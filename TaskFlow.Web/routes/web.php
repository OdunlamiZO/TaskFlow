<?php

use App\Livewire\Analytics;
use App\Livewire\Projects;
use App\Livewire\Tasks;
use App\Models\Token;
use Illuminate\Support\Str;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\Session;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/

Route::get("/", function () {
    return redirect("/tasks");
});

Route::get("/tasks", Tasks::class);

Route::get("/projects", Projects::class);

Route::get("/analytics", Analytics::class);

Route::get("/login", function () {
    Session::put("code_verifier", $code_verifier = bin2hex(random_bytes(32)));
    $code_challenge = hash('sha256', $code_verifier, true);
    Session::put("state", $state = Str::random(32));
    $query = http_build_query([
        "client_id" => env("APP_NAME"),
        "redirect_uri" => env("APP_URL") . "/authorized",
        "response_type" => "code",
        "state" => $state,
        "scope" => "read:user write",
        "code_challenge" =>  rtrim(strtr(base64_encode($code_challenge), '+/', '-_'), "="),
        "code_challenge_method" => "S256",
    ]);
    return redirect(env("AUTH_URL") . "/oauth2/authorize?" . $query);
});

Route::get("/authorized", function (Request $request) {
    $state = Session::pull("state");
    $code_verifier = Session::pull("code_verifier");
    throw_unless(
        strlen($state) > 0 && $state === $request->state,
        InvalidArgumentException::class
    );
    $response = Http::withBasicAuth(env("APP_NAME"), env("APP_SECRET"))
        ->accept("application/json;charset=UTF-8")
        ->asForm()
        ->post(env("AUTH_URL") . "/oauth2/token", [
            "grant_type" => "authorization_code",
            "code" => $request->input("code"),
            "redirect_uri" => env("APP_URL") . "/authorized",
            "code_verifier" => $code_verifier,
        ]);
    $response = $response->json();
    $access_token = $response["access_token"];
    $refresh_token = $response["refresh_token"];
    $response = Http::withHeaders([
        "Accept" => "application/json",
        "Authorization" => "Bearer " . $access_token
    ])->get(env("RESOURCE_URL") . "/user/authenticated");
    $response = $response->json();
    $token = Token::find($response["data"]["username"]);
    if (!$token) {
        $token = Token::create([
            "owner" => $response["data"]["username"],
            "access_token" => $access_token,
            "refresh_token" => $refresh_token,
        ]);
    } else {
        $token->access_token = $access_token;
        $token->refresh_token = $refresh_token;
        $token->save();
    }
    Session::put("user", $token->owner);
    return redirect(Session::has("current_path") ? Session::pull("current_path") : "/tasks");
});

Route::post("/token/revoke", function (Request $request) {
    $user = $request->user;
    $token = Token::find($user);
    if ($token) {
        Http::withBasicAuth(env("APP_NAME"), env("APP_SECRET"))
            ->withQueryParameters([
                "token" => $token->refresh_token
            ])->post(env("AUTH_URL") . "/oauth2/revoke");
        $clean = $request->clean;
        if ($clean) {
            $token->delete();
        }
    }
});
