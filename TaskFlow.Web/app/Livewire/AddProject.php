<?php

namespace App\Livewire;

use App\Helpers\TaskFlowResource;
use Illuminate\Support\Facades\Session;
use Livewire\Component;

class AddProject extends Component
{
    public $title;

    public $deadline;

    public function add()
    {
        $apiResponse = TaskFlowResource::request(
            "/project/add",
            "POST",
            [
                "json" => [
                    "title" => $this->title,
                    "deadline" => $this->deadline
                ]
            ]
        );
        if (is_null($apiResponse)) {
            // save form data and display on login
            Session::put("current_path", "/projects");
            $this->redirect("/login");
            return;
        }
        if ($apiResponse["status"] === "SUCCESS") {
            $this->title = null;
            $this->deadline = null;
            $this->dispatch("modal-clear");
            $this->dispatch("update-projects");
            $this->dispatch("modal-close");
        } else {
            foreach ($apiResponse["data"]["details"] as $id => $message) {
                $this->dispatch("error-show", id: $id, message: $message);
            }
        }
    }

    public function render()
    {
        return view('livewire.add-project');
    }
}
