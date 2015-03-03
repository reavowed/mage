/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.abilities.keyword;

import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.constants.Outcome;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.Target;
import mage.target.TargetPermanent;

/**
 * Exploit is the signature ability of the blue-black Silumgar clan. When a creature with exploit 
 * enters the battlefield, you may sacrifice a creature you control.
 * 
 * But you're not just sacrificing your loyal minions for fun. Each creature with exploit has 
 * another ability that gives you a benefit when it "exploits a creature." This means when you
 * sacrifice a creature because of its exploit ability. That ability doesn't trigger if you 
 * sacrifice a creature for any other reason, including the exploit ability of a different creature.
 * 
 * You can sacrifice any creature you control when the exploit ability resolves, including the creature
 * with exploit itself. You don't have to sacrifice a creature if you don't want to. If you do, you choose
 * which one as the exploit ability resolves. To get the most out of your minions, look for creatures
 * with abilities that give you an added benefit when they die.
 * 
 * @author LevelX2
 */
public class ExploidAbility extends EntersBattlefieldTriggeredAbility {
    
    public ExploidAbility() {
        super(new ExploidEffect(), true);
    }
    
    public ExploidAbility(final ExploidAbility ability) {
        super(ability);
    }
    
    @Override
    public ExploidAbility copy() {
        return new ExploidAbility(this);
    }    

    @Override
    public String getRule() {
        return "Exploit <i>(When this creature enters the battlefield, you may sacrifice a creature.)</i>";
    }
    
}

class ExploidEffect extends OneShotEffect {
    
    public ExploidEffect() {
        super(Outcome.Detriment);
        this.staticText = "you may sacrifice a creature";
    }
    
    public ExploidEffect(final ExploidEffect effect) {
        super(effect);
    }
    
    @Override
    public ExploidEffect copy() {
        return new ExploidEffect(this);
    }
    
    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Target target = new TargetPermanent(1, 1, new FilterControlledCreaturePermanent("creature to exploid"), true);
            if (target.canChoose(source.getSourceId(), controller.getId(), game)) {
                controller.chooseTarget(Outcome.Sacrifice, target, source, game);
                Permanent permanent = game.getPermanent(target.getFirstTarget());
                if (permanent != null ) {
                    if (permanent.sacrifice(source.getSourceId(), game)) {
                        game.fireEvent(GameEvent.getEvent(GameEvent.EventType.EXPLOIDED_CREATURE, permanent.getId(), source.getSourceId(), controller.getId()));
                    }
                }                
            }
            return true;
        }        
        return false;
    }
}
