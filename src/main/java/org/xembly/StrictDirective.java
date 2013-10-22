/**
 * Copyright (c) 2013, xembly.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the xembly.org nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.xembly;

import com.jcabi.aspects.Immutable;
import java.util.Collection;
import java.util.Collections;
import lombok.EqualsAndHashCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * STRICT directive.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.3
 */
@Immutable
@EqualsAndHashCode(of = "number")
final class StrictDirective implements Directive {

    /**
     * Number of nodes we're expecting.
     */
    private final transient int number;

    /**
     * Public ctor.
     * @param nodes Number of node expected
     */
    StrictDirective(final int nodes) {
        this.number = nodes;
    }

    @Override
    public String toString() {
        return String.format("STRICT \"%d\"", this.number);
    }

    @Override
    public Collection<Node> exec(final Document dom,
        final Collection<Node> current) throws ImpossibleModificationException {
        if (current.size() != this.number) {
            if (current.isEmpty()) {
                throw new ImpossibleModificationException(
                    String.format(
                        "no current nodes while %d expected", this.number
                    )
                );
            }
            if (current.size() == 1) {
                throw new ImpossibleModificationException(
                    String.format(
                        "one current node '%s' while strictly %d expected",
                        current.iterator().next().getNodeName(), this.number
                    )
                );
            }
            throw new ImpossibleModificationException(
                String.format(
                    "%d current nodes [%s] while strictly %d expected",
                    current.size(), this.names(current), this.number
                )
            );
        }
        return Collections.unmodifiableCollection(current);
    }

    /**
     * Get node names as a string.
     * @param nodes Collection of nodes
     * @return Text presentation of them
     */
    private String names(final Iterable<Node> nodes) {
        final StringBuilder text = new StringBuilder(0);
        for (final Node node : nodes) {
            if (text.length() > 0) {
                text.append(", ");
            }
            final Node parent = node.getParentNode();
            if (parent != null) {
                text.append(parent.getNodeName());
            }
            text.append('/').append(node.getNodeName());
        }
        return text.toString();
    }

}
