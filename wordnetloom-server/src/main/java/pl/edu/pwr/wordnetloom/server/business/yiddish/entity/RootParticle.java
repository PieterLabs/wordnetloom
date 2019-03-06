package pl.edu.pwr.wordnetloom.server.business.yiddish.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class RootParticle extends Particle {

    @Column
    private String root;

    public RootParticle() {
    }

    public RootParticle(RootParticle p, YiddishSenseExtension ext) {
        setExtension(ext);
        setPosition(p.getPosition());
        root = p.getRoot();
    }

    public RootParticle(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return root + " ( Root )";
    }
}