<?php

namespace App\Livewire;

use App\Helpers\TaskFlowResource;
use Carbon\Carbon;
use Illuminate\Support\Facades\Session;
use Livewire\Attributes\On;
use Livewire\Component;

class Projects extends Component
{
    public $user;

    public $project = [];

    public $projects;

    public $page = 1;

    public $size = 5;

    public function mount()
    {
        $apiResponse = TaskFlowResource::request("/user/authenticated");
        if (!is_null($apiResponse)) {
            $this->user = $apiResponse["data"];
        } else {
            Session::put("current_path", "/projects");
            $this->redirect("/login");
            return;
        }
        $this->setProjects();
    }

    #[On("update-project")]
    public function updateProject($id)
    {
        foreach ($this->projects as $project) {
            if ($project["id"] === $id) {
                $this->project = $project;
                break;
            }
        }
        if ($this->project["deadline"] !== null) {
            $deadline = Carbon::parse($this->project["deadline"]);
            $this->project["deadline"] = $deadline->format("F d, Y");
        }
        $this->dispatch("modal-open", id: "project");
    }

    #[On("update-projects")]
    public function setProjects()
    {
        $this->projects = TaskFlowResource::request("/project")["data"];
        for ($i = 0; $i < count($this->projects); $i++) {
            $this->projects[$i]["progress"] = TaskFlowResource::request("/project/" . $this->projects[$i]["title"] . "/progress")["data"];
        }
    }

    #[On("next-page")]
    public function nextPage()
    {
        $this->page++;
    }

    #[On("previous-page")]
    public function previousPage()
    {
        $this->page--;
    }

    public function render()
    {
        return view('livewire.projects');
    }
}
