package auth

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/odunlamizo/taskflow.cli/internal/util"
	"github.com/odunlamizo/taskflow.cli/internal/variable"
	"github.com/skratchdot/open-golang/open"
	"golang.org/x/oauth2"
)

func Login(oauth2Config *oauth2.Config, ctx context.Context) {
	var verifier string
	if v, err := util.RandomString(16); err != nil {
		log.Fatal("Error: ", err)
	} else {
		verifier = v
	}
	var state string
	if s, err := util.RandomString(16); err != nil {
		log.Fatal("Error: ", err)
	} else {
		state = s
	}
	url := oauth2Config.AuthCodeURL(state, oauth2.S256ChallengeOption(verifier))
	fmt.Println(fmt.Sprintf("Navigating browser to %s", url))
	time.Sleep(2 * time.Second)
	open.Start(url)
	shutdown := make(chan bool)
	server := &http.Server{Addr: fmt.Sprintf(":%d", variable.GetGlobal().Server.Port)}
	go func() {
		okToShutdown := <-shutdown
		if okToShutdown {
			if err := server.Shutdown(ctx); err != nil {
				log.Fatal("Error: ", err)
			}
		}
	}()
	http.HandleFunc("/authorized", func(w http.ResponseWriter, r *http.Request) {
		code := r.URL.Query().Get("code")
		if token, err := oauth2Config.Exchange(ctx, code, oauth2.VerifierOption(verifier)); err != nil {
			log.Fatal("Error: ", err)
		} else {
			if err := util.WriteToken(token); err != nil {
				log.Fatal("Error: ", err)
			}
			fmt.Println("\nTaskFlow says welcome!")
		}
		shutdown <- true
	})
	server.ListenAndServe()
}
