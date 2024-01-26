<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="{{ asset('css/main.css') }}" />
    <link rel="stylesheet" href="{{ asset('css/header.css') }}" />
    @stack("styles")
    @livewireStyles
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
    <script src="https://kit.fontawesome.com/26a4e79a04.js" crossorigin="anonymous"></script>
    <script src="{{ asset('js/header.js') }}"></script>
    <script src="{{ asset('js/logout.js') }}"></script>
    <script>
        // Variables declaration
        let edit;
        let modal;
        let select;
        let dateInput;
        let isDatePickerHidden = true;
    </script>
    @stack("scripts")
    <title>@stack("title")</title>
</head>

<body>
    {{ $slot }}
    <script>
        $(function() {
            $("input.will_validate").on("focus", function(event) {
                Livewire.dispatch('error-hide', {
                    id: event.target.id
                })
            });
        });
    </script>
    @livewireScripts
</body>

</html>
