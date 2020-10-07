# Octo Events

An application that receives GitHub Webhooks, store them in a database and serve them through API.

## Usage

First, install `ngrok`, then run `ngrok http 9090` (if you want to change the application port, change the `octoevents.config.AppFactory.PORT` variable.).

Then run this app using `./gradlew run` (Linux/Mac) or `gradlew.bat run` (Windows).

Finally, create a new webhook on GitHub repository using the url `ngrok-provided-url/events`,
set the content type to `application/json`, and on the trigger events selection, choose `Let me select individual events`
and mark just `Issues` and `Issue comments`, then confirm creation.

## How it works

The `POST /events` route receive a webhook from GitHub and save it in a database which is defined in the `application.properties` file.

Then, the events of a specific issue can be accessed through `GET /issues/{issue-number}/events`. 

## Testing

To run all the tests in this application, just run `./gradlew test` or `gradlew.bat test`.

The Integration tests run in the port 9999 and use an in-memory h2 database.