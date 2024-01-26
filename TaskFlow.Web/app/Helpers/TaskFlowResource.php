<?php

namespace App\Helpers;

use App\Models\Token;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Session;

class TaskFlowResource
{
    public static function request(string $url, string $method = "GET", $options = [])
    {
        if (!Session::has("user")) {
            return null;
        }
        $token = Token::find(Session::get("user"));
        if (!$token) {
            return null;
        }
        $response = Http::withHeaders([
            "Accept" => "application/json",
            "Authorization" => "Bearer " . $token->access_token
        ])->send($method, env("RESOURCE_URL") . $url, $options);
        if ($response->status() === 401) {
            $response = Http::withBasicAuth(env("APP_NAME"), env("APP_SECRET"))
                ->accept("application/json;charset=UTF-8")
                ->withQueryParameters(
                    [
                        "grant_type" => "refresh_token",
                        "refresh_token" => $token->refresh_token,
                    ]
                )->post(env("AUTH_URL") . "/oauth2/token");
            Http::withBasicAuth(env("APP_NAME"), env("APP_SECRET"))
                ->withQueryParameters([
                    "token" => $token->refresh_token
                ])->post(env("AUTH_URL") . "/oauth2/revoke");
            if ($response->status() === 400) {
                return null;
            }
            $response = $response->json();
            $token->access_token = $response["access_token"];
            $token->refresh_token = $response["refresh_token"];
            $token->save();
            $response = Http::withHeaders([
                "Accept" => "application/json",
                "Authorization" => "Bearer " . $token->access_token
            ])->send($method, env("RESOURCE_URL") . $url, $options);
        }
        return $response->json();
    }
}
