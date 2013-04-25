# Blog-O-Matic

An LShift blog post generator.

## Examples

    $ lein repl
    ...
    user=> (use 'blog-o-matic.core)
    nil
    user=> (require '[blog-o-matic.scrape :as scrape])
    nil
    user=> (def posts (take 10 (scrape/fetch-posts)))
    #'user/posts
    user=> (def posts-tokenised (map scrape/tokenise posts))
    #'user/posts-tokenised
    user=> (def freqs (reduce build-frequency-map {} posts-tokenised))
    #'user/freqs
    user=> (first (random-sentences freqs))
    "Oracle Java in almost real-time."
    user=> (apply str (interpose " " (take 5 (random-sentences freqs))))
    "JDK yet to fiddle a Meteor, modelling operations, this on top of a user accounts.
    Register Meteor yet another reason to the willing. How different. Group Policy is
    aimed at the parser, it half a Meteor Angular Leaderboard demo into Coffeescript
    Let’s update to this, and Meteorite helps us to work out the comments are
    frustrating problems with vetted apps based on a SIP packet, whenever I am
    indebted to property of FrameLog is quite simple C objects, coming as java was
    trying to the output, an API call, which means you’ve just then we can employ
    standard asynchronous IO in almost real-time. You could get at this on the
    working environment of it is so I’d expect things more comment than testing for
    permission, then write an example would have spent some nice local group policy."

## License

Copyright © 2013 John Wright, LShift Ltd.

Distributed under the Eclipse Public License, the same as Clojure.
