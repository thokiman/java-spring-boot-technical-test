# Docker Compose Spring Boot and MySQL for Fulfilling Backend Java Spring Technical Test

## Environment
We already setup the local environment and we could change it for personal preference :
```bash
MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=root
MYSQLDB_DATABASE=github_db
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=6868
SPRING_DOCKER_PORT=8080
```

Docker will pull the MySQL and Spring Boot images (if our machine does not have it before).

## Run the System
We can easily run the whole with only a single command:
```bash
docker-compose up
```

Docker will pull the MySQL and Spring Boot images (if our machine does not have it before).

The services can be run on the background with command:
```bash
docker-compose up -d
```

## Stop the System
Stopping all the running containers is also simple with a single command:
```bash
docker-compose down
```

If you need to stop and remove all containers, networks, and all images used by any service in <em>docker-compose.yml</em> file, use the command:
```bash
docker-compose down --rmi all
```

## API
Get all history of exported document:
```bash
curl --location --request GET 'http://localhost:6868/api/history'
```

- Sample JSON Response :
    - Bad request
        - {
          "status": 404,
          "message": "No exported PDF found at database. Please export PDF file at least 1 to record.",
          "timeStamp": 1685274562216
          }
    - Success request
        - [
          {
          "author": "random",
          "document": "1685273531_RANDOM_github.pdf",
          "created_at": "2023-05-28 11:32:12"
          },
          {
          "author": "random",
          "document": "1685273537_RANDOM_github.pdf",
          "created_at": "2023-05-28 11:32:17"
          },
          {
          "author": "random",
          "document": "1685273559_RANDOM_github.pdf",
          "created_at": "2023-05-28 11:32:40"
          },
          {
          "author": "random",
          "document": "1685273575_RANDOM_kafka.pdf",
          "created_at": "2023-05-28 11:32:56"
          }
          ]

Download existing document that had been exported in the past :
```bash
curl --location --request POST 'http://localhost:6868/api/old-doc' \
--header 'Content-Type: application/json' \
--data-raw '{}'
```

- Sample JSON Response :
    - Bad request
        - {
          "status": 404,
          "message": "Error",
          "timeStamp": 1685274562216
          }
    - Success request
        - Exported PDF document with format <Timestamp>_OLD_DOCUMENTS.zip

Download new document that will be exported :
```bash
curl --location --request POST 'http://localhost:6868/api/new-doc' \
--header 'Content-Type: application/json' \
--data-raw '{
    "author":"random",
    "query": "random",
    "sort":"followers",
    "order":"desc",
    "per_page":100,
    "page":1
}'
```
- JSON request payload :
    - author is optional key.
    - query key is required key due Github API policy.
    - sort key is optional key, where the value contains "followers", "repositories", or "joined"
    - order key is optional key, where the value contains "asc" or "desc"
    - per_page is optional key, where the value must be greater than or equal to 100
    - page is optional key, where the value must be greater that 0


- Default JSON request payload :
    - author : "NONE"
    - sort : "asc"
    - order : "followers"
    - per_page : 100
    - page : 1

    
- Sample JSON Response :
    - Bad request
        - {
          "status": 404,
          "message": "Error",
          "timeStamp": 1685274562216
          }

    - Success request
        - Exported PDF document with format <Timestamp>_<Author>_<Query>.pdf
