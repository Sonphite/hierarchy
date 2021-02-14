# Getting Started

### Documentation

We in Amazing Co need to model how our company is structured so we can do awesome stuff.We have a root node (only one) and several
children nodes, each one with its own children as well. It's a tree-based structure. Something like:

``` 
  root
  /  \
 a    b
 |
 c

```

* We need two HTTP APIs that will serve the two basic operations:
* Get all descendant nodes of a given node (the given node can be anyone in the tree structure).
* Change the parent node of a given node (the given node can be anyone in the tree structure).

They need to answer quickly, even with tons of nodes. Also, we can't afford to lose this information, so some sort of persistence is required.
Each node should have the following info:
* node identification
* who is the parent node
* who is the root node
* the height of the node. In the above example, height(root) = 0 and height(a) == 1.


### Curl requests

Create a node for a specified parentNode
POST {host}:{port}/create?parentNodeId={targetParentNodeId}
```shell script
curl -X POST 'http://localhost:8080/create?parentNodeId=100e3d76-ba8d-4c17-8349-d020e6c07182' -H 'content-type: application/json'
```

Get all nodes
GET {host}:{port}/all
```shell script
curl -X GET http://localhost:8080/all   
```

Update parent of a node to new parent 
PUT {host}:{port}/nodeId/targetParentNodeId
```shell script
curl -X PUT http://localhost:8080/81000d5b-4de1-475b-8242-d9cb4acdd616/31ba6e6c-8559-4ca9-a4f8-e3b77395abef/ 
```

GET info for a node
GET {host}:{port}/{nodeId}
```shell script
curl -X GET http://localhost:8080/fbaf4341-43aa-4e92-b5b3-4900c1824d0d 
```

Get descendants of node
GET {host}:{port}/{nodeId}/children
```shell script
curl -X GET http://localhost:8080/e049a9e9-fc16-4d79-9ce2-0e32b641230e/children 
```
###Maven build and integration tests run
```shell script
$ mvn clean install
```


###Build Docker image
```shell script
docker build -t springio/gs-spring-boot-docker .  
```

```shell script
 docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 -t springio/gs-spring-boot-docker

```