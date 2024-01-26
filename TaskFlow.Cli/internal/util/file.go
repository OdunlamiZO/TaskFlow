package util

import (
	"encoding/json"
	"log"
	"os"
	"path/filepath"

	"golang.org/x/oauth2"
)

var authPath string

func init() {
	homeDir, err := os.UserHomeDir()
	if err != nil {
		log.Fatal("Error: ", err)
	}
	authPath = filepath.Join(homeDir, "taskflow", "auth.json")
	err = os.MkdirAll(filepath.Dir(authPath), 0700)
	if err != nil {
		log.Fatal("Error: ", err)
	}
}

func WriteToken(token *oauth2.Token) error {
	b, err := json.Marshal(token)
	if err != nil {
		return err
	}
	err = os.WriteFile(authPath, b, 0600)
	if err != nil {
		return err
	}
	return nil
}

func ReadToken() (*oauth2.Token, error) {
	b, err := os.ReadFile(authPath)
	if err != nil {
		return nil, err
	}
	var token oauth2.Token
	err = json.Unmarshal(b, &token)
	if err != nil {
		return nil, err
	}
	return &token, nil
}
