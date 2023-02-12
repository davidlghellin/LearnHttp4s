# HTTP4S

https://www.youtube.com/watch?v=v_gv6LsWdT0

```
http get  'http://localhost:8888/movies?director=Zack%20Snyder&year=2021'

curl      'http://localhost:8888/movies?director=Zack%20Snyder&year=2021'
```

Para hacer el docker
```shell
sbt docker:publishLocal
```
Para levantar el docker (no consigo acceder posible error en algún param)
```shell
docker run -p 8888:8888 003-scala-http4s-rock-the-jvm:0.0.1-SNAPSHOT
```
