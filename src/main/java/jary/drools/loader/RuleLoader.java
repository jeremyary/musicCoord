package jary.drools.loader;

import org.drools.builder.KnowledgeBuilder;

/***
 * provide interface for loading rule files from various locations (classpath, persistence, outside war, etc.)
 * and types (DRL, XML, etc.)
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
public interface RuleLoader {

    /**
     * Provider a {@link org.drools.builder.KnowledgeBuilder} with rule resource loaded in
     *
     * @return knowledge builder populated with rules, ready for configuration
     */
    public KnowledgeBuilder load(String resource);
}
