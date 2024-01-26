# TaskFlow.Web

TaskFlow frontend application built with PHP Laravel.

## Getting Started

To start a development server, please follow these simple steps.

### Prerequisites

Here is what you need to be able to run TaskFlow.Web

* [PHP](https://www.php.net/manual/en/install.php) (Version: 8.1+)
* [Composer](https://getcomposer.org/)
* [Node](https://nodejs.org/en/download)
* [SQLite](https://www.sqlite.org/download.html)

### Configure Environment

Create a .env configuration file and copy the values from .env.example file into it.

### Database Setup

Having installed SQLite, , you need to make ready the database by doing the following:

1. Configure Laravel to use sqlite database driver by adding the following to the .env configuration file

```
DB_CONNECTION=sqlite
DB_DATABASE=/path/to/database.sqlite
```

2. Run the application's database migration, as follow, which will create the application's database tables

```
php artisan migrate
```

### Running the Application

Start server using the following:

```
php artisan serve --port=8080
```

Access application via http://127.0.0.1:8080
