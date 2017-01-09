package com.king.net.solr.similarity;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.SmallFloat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rongjoker
 * @date:2016-1-29 下午4:57:45
 * @company:kingnet
 * @description:针对applist搜索进行重新打分
 * @version:0.0.1
 */
public class ApplistSolrSimilarity extends TFIDFSimilarity {
	
	 /** Cache of decoded bytes. */
	  private static final float[] NORM_TABLE = new float[256];

	  static {
	    for (int i = 0; i < 256; i++) {
	      NORM_TABLE[i] = SmallFloat.byte315ToFloat((byte)i);
	    }
	  }

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	protected boolean discountOverlaps = true;

	@Override
	public float coord(int overlap, int maxOverlap) {
		return overlap / (float) maxOverlap;
	}

	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		return (float) (1.0 / Math.sqrt(sumOfSquaredWeights));
	}
	
	
	
	@Override
	public float lengthNorm(FieldInvertState state) {
		
	    final int numTerms;
	    if (discountOverlaps)
	      numTerms = state.getLength() - state.getNumOverlap();
	    else
	      numTerms = state.getLength();
	    
//		float _lengthNorms = 1f;
//		if(numTerms>1)
//			_lengthNorms = ((float) (1.0 / Math.cbrt(numTerms)));
//		else
//			_lengthNorms = ((float) (1.0 / Math.cbrt(1.5d)));
	    
	    
	    float _lengthNorms =  (float) (1.0 / Math.cbrt(numTerms+1));
	    
	    float lengthNorm = state.getBoost() * _lengthNorms;
	    
	    //fieldNorm==lengthNorm
//	    LOG.error("numTerms:{},fieldNorm:{}",numTerms,lengthNorm);
	    return lengthNorm;
		
	}

	/**
	 * 逻辑修改为：如果分词长度大于1，则+1，来减弱这项的影响
	 */
	public float lengthNorm_bk(FieldInvertState state) {

		final int numTerms;
		if (discountOverlaps)
			numTerms = state.getLength() - state.getNumOverlap();
		else
			numTerms = state.getLength();
		
//		float _lengthNorms = ((float) (1.0 / Math.sqrt(numTerms)));
		
		float _lengthNorms = 1f;
		if(numTerms>1)
			_lengthNorms = ((float) (1.0 / Math.cbrt(numTerms)));
		else
			_lengthNorms = ((float) (1.0 / Math.cbrt(1.5d)));
		
		
//		float _lengthNorms = (float)(1.0 / Math.cbrt((double)(numTerms/10.0+1)));
		
		float lengthNorms = state.getBoost() * _lengthNorms;//fieldNorm==lengthNorms

//		LOG.error("numTerms:{},fieldNorm:{},getBoost:{}" ,numTerms, lengthNorms,state.getBoost());

		return lengthNorms;
	}

	/**
	 * 关键词出现的频率对打分的影响，设置为1，则忽略关键词出现的频率
	 */
	public float tf(float freq) {
//		float tf_val =  (float) Math.sqrt(freq);
		return 1;
	}

	@Override
	public float sloppyFreq(int distance) {
		return 1.0f / (distance + 1);
	}

	@Override
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		return 1;
	}

	/**
	 * 词条在solr库出现的文档数量，越少表明越具有辨识度
	 */
	@Override
	public float idf(long docFreq, long numDocs) {
		float idf_val = (float) (Math.log(numDocs / (double) (docFreq + 1)) + 1.0);
//		LOG.error("idf_val:{},docFreq:{},numDocs:{}",idf_val,docFreq,numDocs);
		return idf_val;
	}

  public void setDiscountOverlaps(boolean v) {
	    discountOverlaps = v;
	  }

	  /**
	   * Returns true if overlap tokens are discounted from the document's length. 
	   * @see #setDiscountOverlaps 
	   */
	  public boolean getDiscountOverlaps() {
	    return discountOverlaps;
	  }

	@Override
	public float decodeNormValue(long norm) {
		float dec = NORM_TABLE[(int) (norm & 0xFF)];
//		LOG.error("norm:{},dec:{}",norm,dec);
		return dec;
	}

	@Override
	public long encodeNormValue(float f) {
		long enc =  SmallFloat.floatToByte315(f);
//		LOG.error("f:{},enc:{}",f,enc);
		return enc;
	}
	
	

}
