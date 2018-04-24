/**
 * The Greedy scheduler is an extension of the
 * {@link de.naju.adebar.model.events.rooms.scheduling.slacker.SlackerParticipantListValidator
 * Slacker} and may operate on
 * {@link de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification
 * ExtendedRoomSpecification} as well. This comes by the cost of greedy memory consumption and a
 * suboptimal performance however.
 *
 * @author Rico Bergmann
 * @see de.naju.adebar.model.events.rooms.scheduling.slacker.SlackerParticipantListValidator
 *      SlackerParticipantValidator
 * @see de.naju.adebar.model.events.rooms.scheduling.greedy.GreedyParticipantListValidator
 *      GreedyParticipantListValidator
 */
package de.naju.adebar.model.events.rooms.scheduling.greedy;
