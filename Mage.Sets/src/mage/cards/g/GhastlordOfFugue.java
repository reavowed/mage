
package mage.cards.g;

import java.util.UUID;
import mage.MageInt;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.common.DealsCombatDamageToAPlayerTriggeredAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.combat.CantBeBlockedSourceEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.game.Game;
import mage.players.Player;
import mage.target.common.TargetCardInHand;

/**
 *
 * @author jeffwadsworth
 */
public final class GhastlordOfFugue extends CardImpl {

    public GhastlordOfFugue(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{U/B}{U/B}{U/B}{U/B}{U/B}");
        this.subtype.add(SubType.SPIRIT);
        this.subtype.add(SubType.AVATAR);

        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // Ghastlord of Fugue can't be blocked.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new CantBeBlockedSourceEffect(Duration.WhileOnBattlefield)));

        // Whenever Ghastlord of Fugue deals combat damage to a player, that player reveals their hand. You choose a card from it. That player exiles that card.
        this.addAbility(new DealsCombatDamageToAPlayerTriggeredAbility(new GhastlordOfFugueEffect(), false, true));

    }

    public GhastlordOfFugue(final GhastlordOfFugue card) {
        super(card);
    }

    @Override
    public GhastlordOfFugue copy() {
        return new GhastlordOfFugue(this);
    }
}

class GhastlordOfFugueEffect extends OneShotEffect {

    public GhastlordOfFugueEffect() {
        super(Outcome.Benefit);
        staticText = "that player reveals their hand. You choose a card from it. That player exiles that card";
    }

    public GhastlordOfFugueEffect(final GhastlordOfFugueEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player targetPlayer = game.getPlayer(getTargetPointer().getFirst(game, source));
        Player controller = game.getPlayer(source.getControllerId());
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (targetPlayer != null
                && sourceObject != null
                && controller != null) {

            // reveal hand of target player 
            targetPlayer.revealCards(sourceObject.getName(), targetPlayer.getHand(), game);

            // You choose a card from it
            TargetCardInHand target = new TargetCardInHand(new FilterCard());
            target.setNotTarget(true);
            Card chosenCard = null;
            if (controller.choose(Outcome.Benefit, targetPlayer.getHand(), target, source, game)) {
                chosenCard = game.getCard(target.getFirstTarget());
            }
            if (chosenCard != null) {
                controller.moveCards(chosenCard, Zone.EXILED, source, game);
            }
            return true;
        }
        return false;
    }

    @Override
    public GhastlordOfFugueEffect copy() {
        return new GhastlordOfFugueEffect(this);
    }

}
