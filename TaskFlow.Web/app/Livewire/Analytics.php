<?php

namespace App\Livewire;

use App\Helpers\TaskFlowResource;
use Illuminate\Support\Facades\Session;
use Livewire\Component;

class Analytics extends Component
{
    public $user;

    public function mount()
    {
        $apiResponse = TaskFlowResource::request("/user/authenticated");
        if (!is_null($apiResponse)) {
            $this->user = $apiResponse["data"];
        } else {
            Session::put("current_path", "/analytics");
            $this->redirect("/login");
            return;
        }
    }

    public function render()
    {
        return view('livewire.analytics');
    }
}
