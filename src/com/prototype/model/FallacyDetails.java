package com.prototype.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FallacyDetails {
	
	@Id
	@GeneratedValue
	private int fallacyDetailsId;
	
	private int adHomniemUpvotes;
	private int adHomniemDownvotes;
	private int ignorantumUpvotes;
	private int ignorantumDownvotes;
	private int adpopulumUpvotes;
	private int adpopulumDownvotes;
	private int argFromAuthorityUpvotes;
	private int argFromAuthorityDownvotes;
	private int generalizationUpvotes;
	private int generalizationDownvotes;
	private int slipperySlopeUpvotes;
	private int slipperSlopeDownvotes;
	private int strawmanUpvotes;
	private int strawmanDownvotes;
	private int redHerringUpvotes;
	private int redHerringDownvotes;
	private int falseDichotomyUpvotes;
	private int falseDichotomyDownvotes;
	private int circularReasoningUpvotes;
	private int circularReasoningDownvotes;
	
	public int getFallacyDetailsId() {
		return fallacyDetailsId;
	}
	public void setFallacyDetailsId(int fallacyDetailsId) {
		this.fallacyDetailsId = fallacyDetailsId;
	}
	public int getAdHomniemUpvotes() {
		return adHomniemUpvotes;
	}
	public void setAdHomniemUpvotes(int adHomniemUpvotes) {
		this.adHomniemUpvotes = adHomniemUpvotes;
	}
	public int getAdHomniemDownvotes() {
		return adHomniemDownvotes;
	}
	public void setAdHomniemDownvotes(int adHomniemDownvotes) {
		this.adHomniemDownvotes = adHomniemDownvotes;
	}
	public int getIgnorantumUpvotes() {
		return ignorantumUpvotes;
	}
	public void setIgnorantumUpvotes(int ignorantumUpvotes) {
		this.ignorantumUpvotes = ignorantumUpvotes;
	}
	public int getIgnorantumDownvotes() {
		return ignorantumDownvotes;
	}
	public void setIgnorantumDownvotes(int ignorantumDownvotes) {
		this.ignorantumDownvotes = ignorantumDownvotes;
	}
	public int getAdpopulumUpvotes() {
		return adpopulumUpvotes;
	}
	public void setAdpopulumUpvotes(int adpopulumUpvotes) {
		this.adpopulumUpvotes = adpopulumUpvotes;
	}
	public int getAdpopulumDownvotes() {
		return adpopulumDownvotes;
	}
	public void setAdpopulumDownvotes(int adpopulumDownvotes) {
		this.adpopulumDownvotes = adpopulumDownvotes;
	}
	public int getArgFromAuthorityUpvotes() {
		return argFromAuthorityUpvotes;
	}
	public void setArgFromAuthorityUpvotes(int argFromAuthorityUpvotes) {
		this.argFromAuthorityUpvotes = argFromAuthorityUpvotes;
	}
	public int getArgFromAuthorityDownvotes() {
		return argFromAuthorityDownvotes;
	}
	public void setArgFromAuthorityDownvotes(int argFromAuthorityDownvotes) {
		this.argFromAuthorityDownvotes = argFromAuthorityDownvotes;
	}
	public int getGeneralizationUpvotes() {
		return generalizationUpvotes;
	}
	public void setGeneralizationUpvotes(int generalizationUpvotes) {
		this.generalizationUpvotes = generalizationUpvotes;
	}
	public int getGeneralizationDownvotes() {
		return generalizationDownvotes;
	}
	public void setGeneralizationDownvotes(int generalizationDownvotes) {
		this.generalizationDownvotes = generalizationDownvotes;
	}
	public int getSlipperySlopeUpvotes() {
		return slipperySlopeUpvotes;
	}
	public void setSlipperySlopeUpvotes(int slipperySlopeUpvotes) {
		this.slipperySlopeUpvotes = slipperySlopeUpvotes;
	}
	public int getSlipperSlopeDownvotes() {
		return slipperSlopeDownvotes;
	}
	public void setSlipperSlopeDownvotes(int slipperSlopeDownvotes) {
		this.slipperSlopeDownvotes = slipperSlopeDownvotes;
	}
	public int getStrawmanUpvotes() {
		return strawmanUpvotes;
	}
	public void setStrawmanUpvotes(int strawmanUpvotes) {
		this.strawmanUpvotes = strawmanUpvotes;
	}
	public int getStrawmanDownvotes() {
		return strawmanDownvotes;
	}
	public void setStrawmanDownvotes(int strawmanDownvotes) {
		this.strawmanDownvotes = strawmanDownvotes;
	}
	public int getRedHerringUpvotes() {
		return redHerringUpvotes;
	}
	public void setRedHerringUpvotes(int redHerringUpvotes) {
		this.redHerringUpvotes = redHerringUpvotes;
	}
	public int getRedHerringDownvotes() {
		return redHerringDownvotes;
	}
	public void setRedHerringDownvotes(int redHerringDownvotes) {
		this.redHerringDownvotes = redHerringDownvotes;
	}
	public int getFalseDichotomyUpvotes() {
		return falseDichotomyUpvotes;
	}
	public void setFalseDichotomyUpvotes(int falseDichotomyUpvotes) {
		this.falseDichotomyUpvotes = falseDichotomyUpvotes;
	}
	public int getFalseDichotomyDownvotes() {
		return falseDichotomyDownvotes;
	}
	public void setFalseDichotomyDownvotes(int falseDichotomyDownvotes) {
		this.falseDichotomyDownvotes = falseDichotomyDownvotes;
	}
	public int getCircularReasoningUpvotes() {
		return circularReasoningUpvotes;
	}
	public void setCircularReasoningUpvotes(int circularReasoningUpvotes) {
		this.circularReasoningUpvotes = circularReasoningUpvotes;
	}
	public int getCircularReasoningDownvotes() {
		return circularReasoningDownvotes;
	}
	public void setCircularReasoningDownvotes(int circularReasoningDownvotes) {
		this.circularReasoningDownvotes = circularReasoningDownvotes;
	}
	
	

}
