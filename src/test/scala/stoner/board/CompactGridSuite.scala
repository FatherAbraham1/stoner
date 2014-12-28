package stoner.board

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import CompactGrid.{setPointValueInBucket,EMPTY_VALUE,WHITE_VALUE,BLACK_VALUE,
                    Bucket, BITS_PER_POINT, POINTS_PER_BUCKET}

@RunWith(classOf[JUnitRunner])
class CompactGridSuite extends FunSuite {
  
  test("setPointValueInBucket on various indices and values") {
    
    //set empty on all indices of a 0000... bucket
    for ( p <- Range(0, CompactGrid.POINTS_PER_BUCKET)) {
      //set the second index to empty
	  assert(0 == setPointValueInBucket(EMPTY_VALUE << p*BITS_PER_POINT,p, EMPTY))
	  assert(0 == setPointValueInBucket(BLACK_VALUE << p*BITS_PER_POINT,p, EMPTY))
	  assert(0 == setPointValueInBucket(WHITE_VALUE << p*BITS_PER_POINT,p, EMPTY))  
    }
	
	//set various indices to EMPTY after prefilled
	var b : Bucket = 15 //15 = 000...1111
	assert(12 == setPointValueInBucket(b, 0, EMPTY)) //12 = 000..1100
    assert(3 == setPointValueInBucket(b, 1, EMPTY)) // 3 = 000..0011
    
    //set the first point with second point prefilled
    b = 12 // 12 = 0000..1100
    assert(12 == setPointValueInBucket(b, 0, EMPTY))// 12 = 0000..1100
    assert(13 == setPointValueInBucket(b, 0, WHITE)) // 13 = 000..1101
    assert(14 == setPointValueInBucket(b, 0, BLACK))//14 = 000..1110
    
    //set the second point with first point prefilled
    b = 3 // 3 = 000...0011
    assert( 3 == setPointValueInBucket(b, 1, EMPTY)) // 3 = 000...0011
    assert( 7 == setPointValueInBucket(b, 1, WHITE))//7 = 000...0111
    assert(11 == setPointValueInBucket(b, 1, BLACK))//11 = 000..1011
  }//end test("setPointValueInBucket on various indices and values")

  val emptyGrid : CompactGrid = new CompactGrid
  
  test("getIndex on various point values") {
    //test the first column
    for(r <- Range(0, STANDARD_ROWS)) {
      assert(r == emptyGrid.getIndex(Position(0,r)))
    }
    
    //test the top row
    for(c <- Range(0, STANDARD_COLUMNS)) {
      assert(STANDARD_ROWS*c == emptyGrid.getIndex(Position(c,0)))
    }
    
    //test the diagonal
    for(d <- Range(0, STANDARD_ROWS)) {
      assert(d*20 == emptyGrid.getIndex(Position(d,d)))  
    }
    
  }//end test("getIndex on various point values")
  
  test("getBucketIndex on various points") {
    for(p <- Range(0,POINTS_PER_BUCKET)) {
      assert(0 == emptyGrid.getBucketIndex(Position(0,p)))
    }
    
    val bucketsPerGrid = 
      emptyGrid.boardDimension.column * emptyGrid.boardDimension.row / POINTS_PER_BUCKET
    
    for(b <- Range(0, bucketsPerGrid)) {
      assert(b == emptyGrid.getBucketIndex(Position(0,b*POINTS_PER_BUCKET)))
    }
  }//end test("getBucketIndex on various points")
  
  test("getPointIndex on various point values") {
    for(p <- Range(0, POINTS_PER_BUCKET)) {
      assert(p == emptyGrid.getPointIndex(Position(0,p)))
    }
    
    assert(0 == emptyGrid.getPointIndex(Position(0,POINTS_PER_BUCKET)))
    assert(1 == emptyGrid.getPointIndex(Position(0,POINTS_PER_BUCKET+1)))
  }//end test("getPointIndex on various point values")
  
  test("set on various points") {
    var pos = Position(0,0)
    assert(emptyGrid.set(pos, WHITE).get(pos) == WHITE)
    assert(emptyGrid.set(pos, BLACK).get(pos) == BLACK)
    
    pos = Position(0,1)
    assert(emptyGrid.set(pos, WHITE).get(pos) == WHITE)
    assert(emptyGrid.set(pos, BLACK).get(pos) == BLACK)
  }//end test("set on various points")
  
  test("flatten of a grid") {
    assert(emptyGrid.flatten.length == 
           emptyGrid.boardDimension.column*emptyGrid.boardDimension.row)
           
    assert(emptyGrid.set(Position(0,0), EMPTY).flatten(0) == EMPTY)
    assert(emptyGrid.set(Position(0,0), WHITE).flatten(0) == WHITE)
    assert(emptyGrid.set(Position(0,0), BLACK).flatten(0) == BLACK)
  }//end test("flatten of a grid")
  
  test("hashCode of a grid") {
    assert((new CompactGrid).hashCode == (new CompactGrid).hashCode)
    
    //check for collision of similar grids
    assert((new CompactGrid).set(Position(0,0), EMPTY) == 
           (new CompactGrid).set(Position(0,0), EMPTY))
    
    assert((new CompactGrid).set(Position(0,0), WHITE) == 
           (new CompactGrid).set(Position(0,0), WHITE))
           
    assert((new CompactGrid).set(Position(0,0), BLACK) == 
           (new CompactGrid).set(Position(0,0), BLACK))
           
    assert((new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), EMPTY) == 
           (new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), EMPTY))
           
    assert((new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), WHITE) == 
           (new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), WHITE))
           
    assert((new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), BLACK) == 
           (new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), BLACK))
    
    assert((new CompactGrid).set(Position(0,0), WHITE).set(Position(0,1), WHITE) == 
           (new CompactGrid).set(Position(0,0), WHITE).set(Position(0,1), WHITE))
           
    assert((new CompactGrid).set(Position(0,0), WHITE).set(Position(0,1), BLACK) == 
           (new CompactGrid).set(Position(0,0), WHITE).set(Position(0,1), BLACK))
           
    //check for non-collision of non-similar grids
    assert((new CompactGrid).set(Position(0,0), EMPTY) != 
           (new CompactGrid).set(Position(0,0), WHITE))
    
    assert((new CompactGrid).set(Position(0,0), EMPTY) != 
           (new CompactGrid).set(Position(0,0), BLACK))
           
    assert((new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), BLACK) != 
           (new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), EMPTY))
           
    assert((new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), BLACK) != 
           (new CompactGrid).set(Position(0,0), EMPTY).set(Position(0,1), WHITE))       
          
  }//end test("hashCode of a grid")
  
  
}//end class CompactGridSuite extends FunSuite

//31337