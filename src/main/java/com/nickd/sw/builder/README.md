#Builder

## Status

Purely an experiment in navigating/building ontologies in a command line interface

## Features?

* Pluggable commands
* Search ability
* New entity creation
* Axiom creation
* Referencing / wookieepedia
* Autocompletion ?
* Undo
* History / Context (scope)



## TODO

* Axiom show - order referenced entities in MOS order (if possible)

## Bugs
### Accidentally give the wrong placeholder:
    2… > Ferrix > Ferrix (112) > Free_Trade_sector >> new Sector &1 https://starwars.fandom.com/wiki/Free_Trade_sector
    3… > Ferrix (112) > Free_Trade_sector > ontologies#&1 >> show
    0) ontologies#&1 Type Sector...

## Thoughts

### Autocompletion
Can't use autocomplete unless its for final symbol as it either needs to be c+p to edit or automatically
runs again which causes problems:

    ont
    0
    find Planet
    new &0 Ferrix https://starwars.fandom.com/wiki/Ferrix
    wiki &0 suggest
    new System &2
    <
    new &0 Kenari https://starwars.fandom.com/wiki/Kenari
    wiki &0 suggest
    <
    add &0 hasT?
    0
    %(@£$%($£%£%$) 
    add &0 Type hasTerrain some Forest // did nothing
    save


### New from suggestion
Should just be able to select the number without the extras - each suggestion should be a **set of axioms**

    >> wiki Ferrix	suggest
    https://starwars.fandom.com/wiki/Ferrix
    0) Canon
    1) Free_Trade_sector
    2) Morlani_system
    ...
    Ferrix (112) >> new Sector &1 https://starwars.fandom.com/wiki/Free_Trade_sector
    Ferrix (112) > Free_Trade_sector >>

Context will have to be more sophisticated!!