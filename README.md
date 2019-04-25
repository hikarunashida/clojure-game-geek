# clojure-game-geek

Practice for [Lacinia](https://github.com/walmartlabs/lacinia) GraphQL framework

## Referring example code

c.f. https://lacinia.readthedocs.io/en/latest/tutorial/

## Usage

``` shell
cd resources
cp cgg-schema.edn.example cgg-schema.edn
```

``` shell
$ lein repl
```

and eval,

``` shell
(start-server)
```

## Option (wip)

To creating the `cgg-schema.edn` by cgg-schema.umlaut,

``` shell
$ lein umlaut lacinia schema resources/cgg-schema.edn
```

and to creating specs by cgg-schema.umlaut,

``` shell
$ lein umlaut spec schema spec lacinia.spec clojure-game-geek.validators clojure-game-geek
```
