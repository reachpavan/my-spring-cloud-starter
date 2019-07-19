# Fibonacci Series Service

###Maven Build
```
./mvw package
```

###Docker
```
docker-compose -f docker-compose.yml -f docker-compose-dev.yml up
```

###Usage
#####Fibonacci Service
```
curl "http://localhost:8080/fibonacci?count=10"
```

######Response:
```
    {
      "series": [0,1,1,2,3,5,8,13,21,34]
    }
```
######Error Response:
```
    {
      "status": "BAD_REQUEST",
      "message": "count must be lesser than or equal to 100000"
    }
```

#####API Health Check Service
``` 
curl "http://localhost:8080/actuator/health"
```
######Response:
```
{ 
  "status":"UP"
}
```

####TODO:
1. Log File Management: Rolling Logs
2. Enable SSL Security Filter
3. Add Jenkins, Sonar & New Relic support
4. Swagger API Documentation
5. HATEOAS response
6. Integrate with Redis or some caching system