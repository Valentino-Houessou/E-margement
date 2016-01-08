# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table batiment (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  son_universite_id         integer,
  constraint pk_batiment primary key (id))
;

create table cours (
  id                        integer auto_increment not null,
  type                      varchar(255),
  heure_debut               datetime(6),
  heure_fin                 datetime(6),
  son_enseignant_id         integer,
  sa_matiere_id             integer,
  sa_salle_id               integer,
  sa_periode_id             integer,
  constraint pk_cours primary key (id))
;

create table enseignant (
  id                        integer auto_increment not null,
  statut                    varchar(255),
  utilisateur_id            integer,
  constraint pk_enseignant primary key (id))
;

create table etudiant (
  id                        integer auto_increment not null,
  numero_etudiant           varchar(255),
  status                    varchar(255),
  utilisateur_id            integer,
  constraint pk_etudiant primary key (id))
;

create table matiere (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  libelle_abregee           varchar(255),
  semestre                  varchar(255),
  nombre_heures             integer,
  constraint pk_matiere primary key (id))
;

create table module (
  id                        bigint auto_increment not null,
  libelle                   varchar(255),
  constraint pk_module primary key (id))
;

create table periode (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  date_de_deut              datetime(6),
  date_fin                  datetime(6),
  constraint pk_periode primary key (id))
;

create table presence (
  id                        integer auto_increment not null,
  emergement                tinyint(1) default 0,
  motif                     varchar(255),
  justificatif              varchar(255),
  son_cours_id              integer,
  son_etudiant_id           integer,
  constraint pk_presence primary key (id))
;

create table promotion (
  id                        integer auto_increment not null,
  annee_scolaire            varchar(255),
  groupe                    varchar(255),
  type                      varchar(255),
  filiere                   varchar(255),
  constraint pk_promotion primary key (id))
;

create table salle (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  son_batiment_id           integer,
  constraint pk_salle primary key (id))
;

create table universite (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  constraint pk_universite primary key (id))
;

create table utilisateur (
  id                        integer auto_increment not null,
  nom                       varchar(255),
  prenom                    varchar(255),
  adresse_mail              varchar(255),
  mot_de_passe              varchar(255),
  date_de_naissance         datetime(6),
  lien_photo                varchar(255),
  constraint pk_utilisateur primary key (id))
;

alter table batiment add constraint fk_batiment_sonUniversite_1 foreign key (son_universite_id) references universite (id) on delete restrict on update restrict;
create index ix_batiment_sonUniversite_1 on batiment (son_universite_id);
alter table cours add constraint fk_cours_sonEnseignant_2 foreign key (son_enseignant_id) references enseignant (id) on delete restrict on update restrict;
create index ix_cours_sonEnseignant_2 on cours (son_enseignant_id);
alter table cours add constraint fk_cours_saMatiere_3 foreign key (sa_matiere_id) references matiere (id) on delete restrict on update restrict;
create index ix_cours_saMatiere_3 on cours (sa_matiere_id);
alter table cours add constraint fk_cours_saSalle_4 foreign key (sa_salle_id) references salle (id) on delete restrict on update restrict;
create index ix_cours_saSalle_4 on cours (sa_salle_id);
alter table cours add constraint fk_cours_saPeriode_5 foreign key (sa_periode_id) references periode (id) on delete restrict on update restrict;
create index ix_cours_saPeriode_5 on cours (sa_periode_id);
alter table presence add constraint fk_presence_sonCours_6 foreign key (son_cours_id) references cours (id) on delete restrict on update restrict;
create index ix_presence_sonCours_6 on presence (son_cours_id);
alter table presence add constraint fk_presence_sonEtudiant_7 foreign key (son_etudiant_id) references etudiant (id) on delete restrict on update restrict;
create index ix_presence_sonEtudiant_7 on presence (son_etudiant_id);
alter table salle add constraint fk_salle_sonBatiment_8 foreign key (son_batiment_id) references batiment (id) on delete restrict on update restrict;
create index ix_salle_sonBatiment_8 on salle (son_batiment_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table batiment;

drop table cours;

drop table enseignant;

drop table etudiant;

drop table matiere;

drop table module;

drop table periode;

drop table presence;

drop table promotion;

drop table salle;

drop table universite;

drop table utilisateur;

SET FOREIGN_KEY_CHECKS=1;

