package uk.co.benkeoghcgd.api.AxiusCore.API.Updating;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;

public class MavenUpdaterMMP extends MavenUpdater {
    public MavenUpdaterMMP(AxiusPlugin plugin, String repositoryURL) {
        super(plugin, repositoryURL);
    }

    @Override
    protected int compare() {
        return super.compareMMP();
    }
}
