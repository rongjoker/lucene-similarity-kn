package com.king.net.solr.similarity;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rongjoker
 * @date:2016-1-29 下午4:57:45
 * @company:
 * @description:
 * @version:0.0.1
 */
public class DownloadsSolrSimilarity extends DefaultSimilarity {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public float coord(int overlap, int maxOverlap) {
		LOG.info("overlap:{};maxOverlap:{}", overlap, maxOverlap);
		return super.coord(overlap, maxOverlap);
	}

	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		LOG.info("sumOfSquaredWeights:{}", sumOfSquaredWeights);
		return super.queryNorm(sumOfSquaredWeights);
	}

	@Override
	public float lengthNorm(FieldInvertState state) {
		return super.lengthNorm(state);
	}

	/**
	 * 关键词出现的频率对打分的影响
	 */
	public float tf(float freq) {
		LOG.info("freq:{}", freq);
		return super.tf(freq);
	}

	public float tf(int freq) {
		LOG.info("freq:{}", freq);
		return super.tf(freq);
	}

	@Override
	public float sloppyFreq(int distance) {
		LOG.info("distance:{}", distance);
		return super.sloppyFreq(distance);
	}

	@Override
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		LOG.info("doc:{},start:{},end:{},payload:{}", doc, start, end, payload);
		return super.scorePayload(doc, start, end, payload);
	}

	@Override
	public float idf(long docFreq, long numDocs) {
		LOG.info("docFreq:{},numDocs:{}", docFreq, numDocs);
		return super.idf(docFreq, numDocs);
	}

	@Override
	public void setDiscountOverlaps(boolean v) {
		LOG.info("v:{}", v);
		super.setDiscountOverlaps(v);
	}

	@Override
	public boolean getDiscountOverlaps() {
		LOG.info("getDiscountOverlaps():{}", getDiscountOverlaps());
		return super.getDiscountOverlaps();
	}

}
