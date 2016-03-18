package com.king.net.solr.similarity;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rongjoker
 * @date:2016-1-29 下午4:57:45
 * @company:kingnet
 * @description:针对applist搜索进行重新打分
 * @version:0.0.1
 */
public class ApplistSolrSimilarity extends DefaultSimilarity {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public float coord(int overlap, int maxOverlap) {
		LOG.info("overlap:{};maxOverlap:{}", overlap, maxOverlap);
		return overlap / (float) maxOverlap;
	}

	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		LOG.info("sumOfSquaredWeights:{}", sumOfSquaredWeights);
		return (float) (1.0 / Math.sqrt(sumOfSquaredWeights));
	}

	/**
	 * 逻辑修改为：如果分词长度大于1，则+1，来减弱这项的影响
	 */
	@Override
	public float lengthNorm(FieldInvertState state) {

		final int numTerms;
		if (discountOverlaps)
			numTerms = state.getLength() - state.getNumOverlap();
		else
			numTerms = state.getLength();
		
//		float _lengthNorms = ((float) (1.0 / Math.sqrt(numTerms)));
		float _lengthNorms = 1f;
		
		if(numTerms>1)
			_lengthNorms = ((float) (1.0 / Math.cbrt(numTerms+1)));
		else
			_lengthNorms = ((float) (1.0 / Math.cbrt(numTerms)));
			
		float lengthNorms = state.getBoost() * _lengthNorms;

		LOG.info("numTerms:{},lengthNorms:{},getBoost:{}" ,numTerms, lengthNorms,state.getBoost());

		return lengthNorms;
	}

	/**
	 * 关键词出现的频率对打分的影响，设置为1，则忽略关键词出现的频率
	 */
	public float tf(float freq) {
		float tf_val =  (float) Math.sqrt(freq);
		LOG.info("freq:{},tf_val:{}", freq,tf_val);
		return 1;
	}

	@Override
	public float sloppyFreq(int distance) {
		LOG.info("distance:{}", distance);
		return 1.0f / (distance + 1);
	}

	@Override
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		LOG.info("doc:{},start:{},end:{},payload:{}", doc, start, end, payload);
		return 1;
	}

	/**
	 * 词条在solr库出现的文档数量，越少表明越具有辨识度
	 */
	@Override
	public float idf(long docFreq, long numDocs) {
		float idf_val = (float) (Math.log(numDocs / (double) (docFreq + 1)) + 1.0);
		LOG.info("docFreq:{},numDocs:{},idf_val:{}", docFreq, numDocs,idf_val);
		return idf_val;
	}

	@Override
	public void setDiscountOverlaps(boolean v) {
		LOG.info("v:{}", v);
		discountOverlaps = v;
	}

	@Override
	public boolean getDiscountOverlaps() {
		LOG.info("getDiscountOverlaps():{}", discountOverlaps);
		return discountOverlaps;
	}

}
