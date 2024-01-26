package main

import (
	"context"
	"fmt"
	"log"
	"os"

	"github.com/odunlamizo/taskflow.cli/internal/auth"
	"github.com/odunlamizo/taskflow.cli/internal/util"
	"github.com/odunlamizo/taskflow.cli/internal/variable"
	"github.com/olekukonko/tablewriter"
	"github.com/urfave/cli/v2"
	"golang.org/x/oauth2"
)

var (
	oauth2Config *oauth2.Config
	ctx          context.Context
)

func init() {
	ctx = context.Background()
	oauth2Config = &oauth2.Config{
		ClientID:     variable.GetGlobal().App.Name,
		ClientSecret: "",
		Scopes:       []string{"read:user"},
		Endpoint: oauth2.Endpoint{
			AuthURL:  fmt.Sprintf("%s/oauth2/authorize", variable.GetGlobal().App.Url.Auth),
			TokenURL: fmt.Sprintf("%s/oauth2/token", variable.GetGlobal().App.Url.Auth),
		},
		RedirectURL: fmt.Sprintf("http://127.0.0.1:%d/authorized", variable.GetGlobal().Server.Port),
	}
}

func main() {
	app := &cli.App{
		Name:      "taskflow",
		Usage:     "TaskFlow cli application",
		UsageText: "taskflow [command]",
		Commands: []*cli.Command{
			{
				Name:      "auth",
				Usage:     "Manage authentication",
				UsageText: "taskflow auth [command]",
				Subcommands: []*cli.Command{
					{
						Name:      "login",
						Usage:     "Log in a user",
						UsageText: "taskflow auth login",
						Action: func(cCtx *cli.Context) error {
							auth.Login(oauth2Config, ctx)
							return nil
						},
					},
					{
						Name:  "logout",
						Usage: "Logs out the currently logged in user",
						Action: func(cCtx *cli.Context) error {
							return nil
						},
					},
				},
			},
			{
				Name:      "user",
				Usage:     "Returns authenticated user details",
				UsageText: "taskflow user",
				Action: func(cCtx *cli.Context) error {
					table := tablewriter.NewWriter(os.Stdout)
					token, err := util.ReadToken()
					if err != nil {
						log.Fatal("Error: ", err)
					}
					client := oauth2Config.Client(ctx, token)
					resp, err := client.Get(fmt.Sprintf("%s/user/authenticated", variable.GetGlobal().App.Url.Res))
					if err != nil {
						log.Fatal("Error: ", err)
					}
					defer resp.Body.Close()
					render, err := util.FillTable(resp, table)
					if err != nil {
						log.Fatal("Error: ", err)
					}
					if render {
						table.Render()
					}
					return nil
				},
			},
			{
				Name:      "list",
				Usage:     "List user's resources",
				UsageText: "taskflow list [command]",
				Subcommands: []*cli.Command{
					{
						Name:      "task",
						Usage:     "Returns a list of user's tasks",
						UsageText: "taskflow list task",
						Action: func(cCtx *cli.Context) error {
							table := tablewriter.NewWriter(os.Stdout)
							token, err := util.ReadToken()
							if err != nil {
								log.Fatal("Error: ", err)
							}
							client := oauth2Config.Client(ctx, token)
							resp, err := client.Get(fmt.Sprintf("%s/task", variable.GetGlobal().App.Url.Res))
							if err != nil {
								log.Fatal("Error: ", err)
							}
							defer resp.Body.Close()
							render, err := util.FillTable(resp, table)
							if err != nil {
								log.Fatal("Error: ", err)
							}
							if render {
								table.Render()
							} else {
								fmt.Println(fmt.Sprintf("You seem to have no task! TaskFlow says visit %s/tasks to add a new task.", variable.GetGlobal().App.Url.Web))
							}
							return nil
						},
					},
					{
						Name:      "project",
						Usage:     "Returns a list of user's projects",
						UsageText: "taskflow list project",
						Action: func(cCtx *cli.Context) error {
							table := tablewriter.NewWriter(os.Stdout)
							token, err := util.ReadToken()
							if err != nil {
								log.Fatal("Error: ", err)
							}
							client := oauth2Config.Client(ctx, token)
							resp, err := client.Get(fmt.Sprintf("%s/project", variable.GetGlobal().App.Url.Res))
							if err != nil {
								log.Fatal("Error: ", err)
							}
							defer resp.Body.Close()
							render, err := util.FillTable(resp, table)
							if err != nil {
								log.Fatal("Error: ", err)
							}
							if render {
								table.Render()
							} else {
								fmt.Println(fmt.Sprintf("You seem to have no project! TaskFlow says, visit %s/projects to add a new project.", variable.GetGlobal().App.Url.Web))
							}
							return nil
						},
					},
				},
			},
		},
	}

	if err := app.Run(os.Args); err != nil {
		log.Fatal("Error: ", err)
	}
}
