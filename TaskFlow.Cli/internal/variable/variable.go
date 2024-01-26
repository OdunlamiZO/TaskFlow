package variable

type global struct {
	App    app
	Server server
}

type app struct {
	Name string
	Url  url
}

type url struct {
	Auth string
	Res  string
	Web  string
}

type server struct {
	Port int
}

func GetGlobal() global {
	return global{
		App: app{
			Name: "TaskFlow.Cli",
			Url: url{
				Auth: "http://127.0.0.1:9000",
				Res:  "http://127.0.0.1:8090",
				Web:  "http://127.0.0.1:8080",
			},
		},
		Server: server{
			Port: 8081,
		},
	}
}
