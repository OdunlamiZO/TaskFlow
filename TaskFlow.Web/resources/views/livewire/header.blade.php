<div class="header">
    <div class="flex left">
        <div id="nav_btn">
            <span class="line"></span>
            <span class="line"></span>
            <span class="line"></span>
        </div>
        <a href="/tasks"><img src="{{ asset('images/logo-text.png') }}" alt="Logo" /></a>
    </div>
    <div class="center">
        <div class="empty"></div>
        <div id="nav_search">
            <div id="nav" role="navigation">
                <a href="/tasks" class="{{ request()->is('tasks') ? 'nav_item active' : 'nav_item' }}">Tasks</a>
                <a href="/projects" class="{{ request()->is('projects') ? 'nav_item active' : 'nav_item' }}">Projects</a>
                <a href="/analytics" class="{{ request()->is('analytics') ? 'nav_item active' : 'nav_item' }}">Analytics</a>
            </div>
            <div id="search">
                <input type="text" />
                <div id="search_btn">
                    <svg height="512" viewBox="0 0 512 512" width="512">
                        <title />
                        <path d="M221.09,64A157.09,157.09,0,1,0,378.18,221.09,157.1,157.1,0,0,0,221.09,64Z" style="
							fill: none;
							stroke: #000;
							stroke-miterlimit: 10;
							stroke-width: 32px;
						" />
                        <line style="
							fill: none;
							stroke: #000;
							stroke-linecap: round;
							stroke-miterlimit: 10;
							stroke-width: 32px;
						" x1="338.29" x2="448" y1="338.29" y2="448" />
                    </svg>
                </div>
            </div>
        </div>
    </div>
    <div class="flex right" style="position: relative;">
        <div id="notification_btn">
            <i class="fa-regular fa-bell"></i>
            <span id="message_count">99+</span>
        </div>
        <div id="avatar">
            @php
                $avatar = $user["avatar"]??null
            @endphp
            <img src="{{ $avatar ? 'data:' . $avatar['mimeType'] . ';base64,' . $avatar['data'] : asset('images/avatar_placeholder.png') }}" alt="Me" />
            <div id="nav_ext_btn"></div>
        </div>
        <div id="nav_ext" class="hidden" role="navigation">
            <div class="close_btn">
                <span class="line"></span>
                <span class="line"></span>
            </div>
            <div class="header" style="display: block; text-align: center">{{ $user["name"] }}</div>
            <div>
                <a href="{{ env('AUTH_URL') . '/profile' }}" class="nav_ext_item"><span style="padding-right: 15px;"><i class="fa-regular fa-user" style="color: #808080"></i></span>View Profile</a>
                <a href="#" class="nav_ext_item"><span style="padding-right: 15px;"><i class="fa-solid fa-gear" style="color: #808080"></i></span>Account Settings</a>
                <a href="/logout" class="nav_ext_item" wire:click.prevent="$dispatch('logout')"><span style="padding-right: 15px;"><i class="fa-solid fa-power-off" style="color: #808080"></i></span>Sign Out</a>
            </div>
        </div>
    </div>
</div>
