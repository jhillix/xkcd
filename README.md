
          _           _   ____  ____ ____    ____                _
    __  _| | _____ __| | |  _ \/ ___/ ___|  |  _ \ ___  __ _  __| | ___ _ __
    \ \/ / |/ / __/ _` | | |_) \___ \___ \  | |_) / _ \/ _` |/ _` |/ _ \ '__|
     >  <|   < (_| (_| | |  _ < ___) |__) | |  _ <  __/ (_| | (_| |  __/ |
    /_/\_\_|\_\___\__,_| |_| \_\____/____/  |_| \_\___|\__,_|\__,_|\___|_|

***

## Intro
[xkcd](http://xkcd.com/) RSS Feed for the CLI (Command Line Interface). Not so great CLI interface for xkcd's RSS feeds.

## Build
To build this application:

1. `git clone <repo>`
2. `cd xkcd`
3. `mvn install`

NOTE: This project has an active "dev" branch and a "master" branch. If you want the latest features, if applicable, then
      `git checkout dev` and then build. For _stable_ releases build off of the "master" branch.

## Usage
"master" -> `java -jar xkcd-1.X-jar-with-dependencies.jar`

"dev" -> `java -jar xkcd-1.X-SNAPSHOT-jar-with-dependencies.jar`

## Features

* Bookmarks (Java Preferences API)
* Tab completion (JLine)
* Command history (JLine)