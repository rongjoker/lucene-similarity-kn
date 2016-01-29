package com.king.net.solr.similarity;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;

/**
 * @author rongjoker
 * @date:2016-1-29 下午4:57:45
 * @company:
 * @description:
 * @version:0.0.1
 */
public class DownloadsSolrSimilarity extends DefaultSimilarity {

	@Override
	public float coord(int overlap, int maxOverlap) {
		return super.coord(overlap, maxOverlap);
	}

	@Override
	public float queryNorm(float sumOfSquaredWeights) {
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
		return super.tf(freq);
	}

	@Override
	public float sloppyFreq(int distance) {
		return super.sloppyFreq(distance);
	}

	@Override
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		return super.scorePayload(doc, start, end, payload);
	}

	@Override
	public float idf(long docFreq, long numDocs) {
		return super.idf(docFreq, numDocs);
	}

	@Override
	public void setDiscountOverlaps(boolean v) {
		super.setDiscountOverlaps(v);
	}

	@Override
	public boolean getDiscountOverlaps() {
		return super.getDiscountOverlaps();
	}

}
