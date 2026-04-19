# EnoSpigot

Serveur Minecraft **1.8.8** sur mesure pour le réseau [Enofight](https://github.com/mimiflo/EnoInfrastructure).

EnoSpigot est notre fork de Paper 1.8.8, optimisé pour le PvP compétitif (duels, BedWars, KitPvP...). On maintient notre propre stack de patches pour modifier le serveur selon nos besoins : knockback custom, nouveaux events, optimisations réseau, etc.

## Pourquoi un fork ?

Faire tourner un réseau PvP 1.8 moderne demande des modifications profondes du serveur, bien au-delà de ce qu'un plugin peut faire. Un fork nous donne :

- **Contrôle total** sur le comportement du serveur (combat, mouvement, ticks)
- **Patches custom** maintenus dans `patches/server/` et `patches/api/`
- **Merges upstream faciles** depuis Paper (via PandaSpigot) pour récupérer perf et bugfixes
- **Un seul artefact** : `paperclip.jar` qu'on ship dans le container Docker du gameserver

## Stack des upstreams

```
Spigot (Bukkit + CraftBukkit)
   └── Paper 1.8.8
          └── PandaSpigot   ← upstream visible (hpfxd/PandaSpigot)
                 └── EnoSpigot  ← nos patches Enofight
```

PandaSpigot est lui-même un fork maintenu de Paper 1.8.8 centré sur la stabilité et les perfs du legacy 1.8. On s'appuie dessus pour bénéficier de leur travail tout en ajoutant le nôtre par-dessus.

## Build

**Requis :**
- JDK 17 (uniquement pour builder — le JAR produit reste Java 8 compatible)
- Git
- Bash

### Premier setup

```bash
./panda setup
```

Clone Paper en submodule, remappe les mappings, décompile le serveur Minecraft, applique les patches Spigot/Paper/PandaSpigot/EnoSpigot. **~10 minutes** et quelques centaines de Mo de téléchargement.

### Build du jar

```bash
./panda jar
```

Produit `paperclip.jar` à la racine. Pour le shipper avec le gameserver Enofight :

```bash
cp paperclip.jar ../../docker/gameserver/enospigot.jar
```

### Commandes utiles

| Commande | Alias | Effet |
|----------|-------|-------|
| `./panda setup` | — | Setup complet (init + patch + remap + decompile) |
| `./panda jar` | `./panda j` | Applique les patches et build |
| `./panda patch` | `./panda p` | Applique les patches sans build |
| `./panda rebuild` | `./panda rb` | Régénère `patches/` depuis les sources modifiées |
| `./panda clean` | `./panda c` | Supprime les dossiers générés |
| `./panda continue` | `./panda con` | `git am --continue` ou `git rebase --continue` |

## Ajouter un patch

Le flow Paper/Spigot standard :

1. `cd EnoSpigot-Server` (ou `EnoSpigot-API`)
2. Modifier le code
3. `git add` + `git commit -m "Description courte du patch"`
4. Retour à la racine → `./panda rebuild`
5. Le nouveau patch apparaît dans `patches/server/` (ou `patches/api/`)
6. Commit le patch généré dans le repo `mimiflo/EnoSpigot`

### Convention de marquage

Quand tu modifies du code upstream (Paper / PandaSpigot / CraftBukkit), marque tes changements :

- **Multi-lignes** : `// EnoSpigot start` / `// EnoSpigot end`
- **Multi-lignes avec raison** : `// EnoSpigot start - réduire TPS lag sur chunk unload`
- **Une ligne** : `// EnoSpigot` en fin de ligne

Les patches PandaSpigot existants conservent leurs marqueurs `// PandaSpigot` pour que les merges upstream restent propres.

## Sync avec upstream (PandaSpigot)

```bash
git fetch upstream
git merge upstream/master
```

S'il y a des conflits dans les patches :
1. Résous-les dans `EnoSpigot-Server` / `EnoSpigot-API`
2. `./panda continue` (ou `git am --continue`)
3. `./panda rebuild` pour régénérer `patches/`
4. Commit et push

## Intégration dans EnoInfrastructure

Le JAR buildé est consommé par le gameserver Docker :

```
EnoInfrastructure/
├── docker/gameserver/
│   ├── Dockerfile          → COPY enospigot.jar
│   └── enospigot.jar       ← artefact buildé depuis ce fork
└── vendor/enospigot/       ← ce repo (submodule / clone)
```

Pour déployer une nouvelle version :
1. Build ici : `./panda jar`
2. Copie : `cp paperclip.jar ../../docker/gameserver/enospigot.jar`
3. Commit le jar dans `EnoInfrastructure` et push → déploiement auto via la CI Enofight

## License

Hérite de PandaSpigot / Paper / Spigot. Voir `LICENSE.md`.
