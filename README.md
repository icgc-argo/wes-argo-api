# RDPC-GATEWAY

This service serves as the GraphQL API gateway for the Regional Data Processing Centre. The primary responsibility is to stitch the various data sources and microservices that constitute the RDPC infrastructure into a single unified GraphQL API.

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

## Service List

The gateway "joins" together other Apollo GQL services (Apollo Federation) in the RDPC (Regional Data Processing Center) it is deployed in, unifying their various queries into one single API and enabling the use of cross-service enriching of data at query time.

| Service Name | Environment Variable | Description | Git Repo | Queries | Mutations |
|--------------|----------------------|-------------|----------|---------|-----------|
| workflows | `WORKFLOW_SEARCH_URL` | search for workflow runs in the RDPC | [workflow-search](https://github.com/icgc-argo/workflow-search) | `runs`, `tasks`, `aggregateRuns`, `aggregateTasks` | N/A |
| workflow-management | `WORKFLOW_MANAGEMENT_URL` | manage (start/cancel) runs in the RDPC | [workflow-management](https://github.com/icgc-argo/workflow-management) | N/A |  `startRun`, `cancelRun` |
| song-search | `SONG_SEARCH_URL` | search for analyses and files indexed in RDPC SONG(s) | [song-search](https://github.com/icgc-argo/song-search/) | `analyses`, `files`, `aggregateAnalyses`, `aggregateFiles`, `sampleMatchedAnalysisPairs` | N/A |

## Example Query

An example of this dynamic join at query time is something like this:

```
{
  runs { // top level query resolved in workflow-search
    content {
      runId
      state
      producedAnalyses { // this field is resolved at query time with data from song-search
        analysisId
  		analysisType
        analysisState
      }
  }
}
```

## Tech Stack
- NodeJS
- Typescript
- Express
- Apollo GQL (Apollo Federation)

## Build and Run

### Quick Start

```
# Clone & Init
git clone git@github.com:icgc-argo/rdpc-gateway.git
npm i

# Env Variables (recommended you change these to suit your needs)
cp .env.example .env

# Start Locally
npm start
```

### Build

```
# Local
npm build

# Docker
docker build -t ##REPO##:##TAG## .
```
