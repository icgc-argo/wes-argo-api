def dockerHubRepo = "icgcargo/rdpc-gateway"
def githubRepo = "icgc-argo/rdpc-gateway"
def chartVersion = "1.0.0"
def commit = "UNKNOWN"
def version = "UNKNOWN"
def uikitVersion = "UNKNOWN"


pipeline {
    agent {
        kubernetes {
            label 'rdpc-gateway'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: node
    image: node:12.6.0
    tty: true
  - name: dind-daemon
    image: docker:18.06-dind
    securityContext:
        privileged: true
    volumeMounts:
      - name: docker-graph-storage
        mountPath: /var/lib/docker
  - name: helm
    image: alpine/helm:2.12.3
    command:
    - cat
    tty: true
  - name: docker
    image: docker:18-git
    tty: true
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: docker-sock
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
      type: File
  - name: docker-graph-storage
    emptyDir: {}
"""
        }
    }
    stages {
        stage('Prepare') {
            steps {
                script {
                    commit = sh(returnStdout: true, script: 'git describe --always').trim()
                }
                script {
                    version = sh(returnStdout: true, script: 'cat ./package.json | grep version | cut -d \':\' -f2 | sed -e \'s/"//\' -e \'s/",//\'').trim()
                }
            }
        }
        stage('Build & Publish Develop') {
            when {
                branch "develop"
            }
            steps {
                container('docker') {
                    withCredentials([usernamePassword(credentialsId:'argoDockerHub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                    }

                    // DNS error if --network is default
                    sh "docker build --network=host . -t ${dockerHubRepo}:edge -t ${dockerHubRepo}:${commit}"

                    sh "docker push ${dockerHubRepo}:${commit}"
                    sh "docker push ${dockerHubRepo}:edge"
                }
            }
        }
        stage('deploy to rdpc-collab-dev') {
            when {
                branch "develop"
            }
            steps {
                build(job: "/provision/helm", parameters: [
                     [$class: 'StringParameterValue', name: 'AP_RDPC_ENV', value: 'dev' ],
                     [$class: 'StringParameterValue', name: 'AP_CHART_NAME', value: 'rdpc-gateway'],
                     [$class: 'StringParameterValue', name: 'AP_RELEASE_NAME', value: 'rdpc-gateway'],
                     [$class: 'StringParameterValue', name: 'AP_HELM_CHART_VERSION', value: "${chartVersion}"],
                     [$class: 'StringParameterValue', name: 'AP_ARGS_LINE', value: "--set-string image.tag=${commit}" ]
                ])
            }
        }
        stage('Build & Publish Release') {
            when {
                branch "master"
            }
            steps {
                container('docker') {
                    withCredentials([usernamePassword(credentialsId: 'argoGithub', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                        sh "git tag ${version}"
                        sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/${gitHubRepo} --tags"
                    }

                    withCredentials([usernamePassword(credentialsId:'argoDockerHub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                    }

                    // DNS error if --network is default
                    sh "docker build --network=host . -t ${dockerHubRepo}:latest -t ${dockerHubRepo}:${version}"

                    sh "docker push ${dockerHubRepo}:${version}"
                    sh "docker push ${dockerHubRepo}:latest"
                }
            }
        }
        stage('deploy to rdpc-collab-qa') {
            when {
                branch "master"
            }
            steps {
                build(job: "/provision/helm", parameters: [
                     [$class: 'StringParameterValue', name: 'AP_RDPC_ENV', value: 'qa' ],
                     [$class: 'StringParameterValue', name: 'AP_CHART_NAME', value: 'rdpc-gateway'],
                     [$class: 'StringParameterValue', name: 'AP_RELEASE_NAME', value: 'rdpc-gateway'],
                     [$class: 'StringParameterValue', name: 'AP_HELM_CHART_VERSION', value: "${chartVersion}"],
                     [$class: 'StringParameterValue', name: 'AP_ARGS_LINE', value: "--set-string image.tag=${version}" ]
                ])
            }
        }
    }
}
