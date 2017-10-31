# Intrusion Detection System with Redis

System for detecting if failed login count exceeds a certain threshold 
from single IP within a time period (eg 5 minutes) by examining access logs    

Below interface is used for detecting hack attemps

```java
public interface HackerDetector {

    String parseLogLine(String line);

}
```

parseLogline method returns _IP_ if hack attempt detected. Otherwise _empty string_ is returned. 
 
Sample log line is 

```
1507365137,187.218.83.136,John.Smith,SUCCESS
```

## Design

**Redis** is used as _in-memory cache_ for performance reasons. Only failed login attempts are kept.

(key: "ip:minute") : set(failed-login-timestamp)  tuples are main data structure.

This way redis can expire keys on minute basis.

## Performance

Service is implemented in a _stateless_ fashion, so **horizontal scaling** is possible.  

To get a slight idea about performance some integration tests are also implemented.

Over 5K lines processed on my crappy desktop :) with mobile I5 CPU and      
on laptop with i7 6820HQ CPU around 25K log lines processed per second 
(excluding file read).

| CPU       |  #lines per second |  
|-----------|--------------------|
| mobile I5 | ~6K                |
| i7 6820HQ | ~25K               |
