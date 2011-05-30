# clj-schulze

A Clojure implementation of the Schulze voting method.

## Usage

The `schulze` function is the one you will be calling. Its syntax is

```clojure
(schulze ballots candidates)
```

<!--

The return value of this function is ...

-->

### Input details

`candidates` is a set specifying the valid candidates, i.e. the entries which may appear in ballots.  Each entry in this set must be a keyword.  A simple example is

```clojure
(def candidates #{:a :b :c :d :e})
```

`ballots` is a vector of vectors, each of which lists candidates from the most preferred (the first element of the vector) to the least preferred (the last element).  For example, the vector

```clojure
[:a :b :c]
```

expresses that the voter prefers candidate a over candidate b, and both of these over candidate c.  Ties can be expressed by putting more than one candidate in a set:

```clojure
[:a #{:b :c}]
```

expresses that the voter prefers candidate a over candidates b and c, but has no preference among b and c.

If any valid candidates are omitted from the ballot, it is assumed that (1) the voter has no preference between the omitted candidates, and (2) the voter prefers all listed candidates to all omitted candidates.  Thus with the example set of candidates above, the “canonical” version of the preceding ballot is

```clojure
[:a #{:b :c} #{:d :e}]
```

### Output details

<!-- TODO: write the parts of the program that actually produce output so that this section can be written -->

### Ballot processing details

*There is some leeway in the format of the ballots that are submitted; this section describes the transformations that are automatically applied so you know what you do and don’t need to do to the ballots before you pass them to the “schulze” function.*

Before any processing, each ballot is converted to a canonical form through the following process:

1. All candidates not included in the ballot are added as shown in the example above.
2. Any empty sets (i.e. `#{}`) in the ballot are removed.
3. Any nested sets are flattened out, e.g. `#{:b #{:c :d}}` becomes `#{:b :c :d}`.  (It’s not clear why you would have such a structure in the first place…)
4. Any “bare” elements are put into single-element sets, so that the vector consists entirely of sets.

Given the example set of candidates above, the canonical form of the ballot

```clojure
[:a #{:b #{:e}} #{} #{:d}]
```

is

```clojure
[#{:a} #{:b :e} #{:d} #{:c}]
```

## Further reading

For more information on the Schulze method, see

* [“Schulze method” on Wikipedia](http://en.wikipedia.org/wiki/Schulze_method)
* [Markus Schulze’s website on the Schulze method](http://m-schulze.webhop.net/)

## Project information

clj-schulze is developed by Benjamin Esham ([e-mail](mailto:bdesham@gmail.com)).  The [project website](https://github.com/bdesham/clj-schulze) is hosted on GitHub.

### Release history

Beginning with version 1.0.0, this project will use the [Semantic Versioning](http://semver.org/) specification by Tom Preston-Werner.

* 2011-05-29: Project started

### License

Copyright © 2011, Benjamin Esham. This software is released under the following version of the MIT license:

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following condition: the above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

**The software is provided “as is”, without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.**
