package util

import (
	"encoding/json"
	"errors"
	"io"
	"net/http"
	"strconv"
	"strings"

	"github.com/olekukonko/tablewriter"
	"golang.org/x/text/cases"
	"golang.org/x/text/language"
)

func FillTable(resp *http.Response, table *tablewriter.Table) (bool, error) {
	table.SetAutoFormatHeaders(false)
	table.SetAlignment(tablewriter.ALIGN_CENTER)
	var body map[string]interface{}
	if b, err := io.ReadAll(resp.Body); err != nil {
		return false, err
	} else {
		err := json.Unmarshal([]byte(b), &body)
		if err != nil {
			return false, err
		}
	}
	idata, _ := body["data"]
	if body["status"] == "SUCCESS" {
		if data, ok := idata.(map[string]interface{}); ok {
			avatar := "avatar"
			if _, ok := data[avatar]; ok {
				delete(data, avatar) // remove avatar from response
			}
			header := make([]string, 0, len(data))
			for key := range data {
				header = append(header, cases.Title(language.English).String(key))
			}
			table.SetHeader(header)
			row := make([]string, 0, len(data))
			for _, column := range header {
				row = append(row, data[strings.ToLower(column)].(string))
			}
			table.Append(row)
		} else if data, ok := idata.([]interface{}); ok {
			if len(data) == 0 {
				return false, nil
			}
			var header []string
			for index, isubdata := range data {
				if subdata, ok := isubdata.(map[string]interface{}); ok {
					if index == 0 {
						header = make([]string, 0, len(subdata))
						header = append(header, "Id")
						for key := range subdata {
							if key == "id" {
								continue
							}
							header = append(header, cases.Title(language.English).String(key))
						}
						table.SetHeader(header)
					}
					row := make([]string, 0, len(subdata))
					for _, column := range header {
						if column == "Id" {
							row = append(row, strconv.FormatFloat(subdata[strings.ToLower(column)].(float64), 'f', -1, 64))
						} else {
							row = append(row, subdata[strings.ToLower(column)].(string))
						}
					}
					table.Append(row)
				}
			}
		}
	} else {
		data, _ := idata.(map[string]interface{})
		return false, errors.New(data["message"].(string))
	}
	return true, nil
}
