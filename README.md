stoner (as in Go stone, you hippy)
==================================
A research project to develop an [automated](http://en.wikipedia.org/wiki/Artificial_intelligence) [Go](http://en.wikipedia.org/wiki/Go_(game)) player utilizing machine learning techniques rather than typical tree descent searches.  The primary language will be [Scala](http://www.scala-lang.org).

Problem Formulation
-------------------
The problem I wish to solve is as follows:

* Given a Go board position **P**, without accompanying game history except for illegal moves due to ko, find the move **M** that maximizes the probability of a resulting game win.

I got 99 problems but optimal move estimator given current position state representation ain't one!

Techniques
----------
Instead of writing another run-of-the-mill monte carlo game tree descent search player I want to focus on recent research in machine learning (see section: Prior Art).  

Some research threads I want to focus on are:

1. Most recent research in the ML game player field has focused on training a computer player to predict an "expert" player's next move, e.g. don't create a smart game player just make one that mimics a smart player.  I,however, want to create a player that predicts which side will win given a position.  To turn this oracle into a game player: search over all legal moves **{M}** for a position **P**, and predict the opponents probability of winning given the position resulting from each move (**P** + **m_i**), then choose the move that results in the lowest probability of winning for the opponent.  I think NNs and SVMs would work well.

2. Using ML predictors as a pruners for regular monte carlo tree searchers.

3. Exploit AWS cluster deployment to create different "grades" of player.  If you want to turn the dial up to 11 you can distribute the player across several AWS nodes (possibly using [EMR](http://aws.amazon.com/elasticmapreduce/)).  Or for all the n00bs just have the player deployment to cores on the local host.  Can I get some skynet up in this piece!!!


![](http://cdn.screenrant.com/wp-content/uploads/terminator-5-release-date-new-trilogy.jpg)


Language
--------
Why functional programming you ask??? 

![](http://imgs.xkcd.com/comics/functional.png)

Because I'm trying to make the '60s cool again (free love man).  Seriously: because I wanna whole lotta cuncurrency (see section below) and I think Scala gets me there quickest.

Concurrency Models
------------------
I want some concurrencly  jambalaya: all kinds of spices and flavors.  I would like to use:

1. data parallelism: use cuDNN for GPU Neural networks.
2. actor thread model: [Akka](http://akka.io) (god bless you) actors for multi-core & multi-chip parallelism.
3. lambda architecture: Did somebody say "jump on the bandwagon"?  I wanna use [Spark](https://spark.apache.org) to distribute some ML work.

Prior Art
---------
My automata senseis are:

1. Clark, C., & Storkey, A. (2014) [Teaching Deep Convolutional Neural Networks to Play Go](http://arxiv.org/abs/1412.3409)
2. Rimmel, R., & Teytaud, O., & Lee, C., & Yen, S., & Wang, M., & Tsai, S. (2010) [Current Frontiers in Computer Go](https://hal.inria.fr/inria-00544622/PDF/ct.pdf). Computational Intelligence and AI in Games.

Training Data Set
-----------------
*"Life can only be understood backwards; but it must be lived forwards." - Kierkegaard*

The primary data set for training and evaluation will be the [GoGoD](http://gogodonline.co.uk/) dataset.  It is freakin' awesome, the earliest game is from 196 AD!  Unfortunately, as with most things in life, it ain't free (see INSTALL.md).
