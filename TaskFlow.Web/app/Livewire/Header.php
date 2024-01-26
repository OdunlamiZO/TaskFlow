<?php

namespace App\Livewire;

use Livewire\Attributes\On;
use Livewire\Component;
use App\Models\Token;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Session;

class Header extends Component
{
    public $user;

    #[On("logout")]
    public function logout()
    {
        $user = Session::get("user");
        if ($user) {
            $token = Token::find($user);
            Http::withBasicAuth(env("APP_NAME"), env("APP_SECRET"))
                ->withQueryParameters([
                    "token" => $token->refresh_token
                ])->post(env("AUTH_URL") . "/oauth2/revoke");
            Session::flush();
        }
        $this->dispatch("auth-logout", url: env("AUTH_URL"));
    }

    public function render()
    {
        return view('livewire.header');
    }
}
