# TODO

* update README to include
    * correct “API” information
	* implementation (Clojure) comments
	* notes on the choices made w.r.t. the voting method
* infer the list of candidates from e.g. the list of ballots to avoid passing `candidates` to everything
* see if `total-pairwise-defeats` can be written more elegantly
* wrap all of these functions as a proper library (e.g. use `defn-` as appropriate)
* rewrite ballots-handling functions so that the form `{ballot1 count1, ballot2 count2}` is the fundamental one
