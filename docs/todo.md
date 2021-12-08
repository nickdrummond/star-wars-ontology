# Todo

[back to index](index.md)

* Add Cold War
  

* need to untangle ```of``` and ```transferOf``` and ```passes``` and ```participant```

* Transfer ```of``` object ```from``` personX ```to``` personY - ownership (eg darksaber) - can we then infer the ownedBy?
  * try the Falcon
  * can we also use for TRANSFER of location (journey)?
  * and TRANSFER of information (learning?)?
    * TRANSFER of role (promotion?/defection)
    * TRANSFER into the Force?
    
* losing of limbs (Anakin + Luke)

* change ```escapeOf``` to ```freeingOf``` in rescues as the former implies self-rescue
  - done for Rebels

* Hologram/projector - conversations with remote participants should be made clear
  * use same pattern as disguise? makes it easy to have multiple
  participant exactly 1 ({Bail_Organa} and (using some Holoprojector) and (location...))

* How do we infer that specific people were participants in `Evacuation_of_Garel` - we're using `Spectres` a lot?
  Should we say that all members of an organisation or crew were at that event? What about Battles - Rebel Alliance vs Empire?
  Not all members of a group are a member at that time.

* Can we query for the people someone has met? Probably not as just being at the same event or a member of a group
  depends on the number of people in that event/group
    - ie not everyone in the Empire knows each other
    - not everyone at the Battle of Scariff knows each other

*  Where we use oneofs, there needs to be specified cardinality instead of `some`
   or else the inference does not work - ie a given member cannot be inferred to be related
   BUT - cannot do this with hadMember as it is transitive which makes it non-simple
   Actually, it doesn't work with the Crew in between  
   an example where it does work is `Escape from Death Star` both Solo and Skywalker are inferred participants

* Can we easily query/infer a timeline for a given character?
    * we can easily [query for the events](docs/events.md) using simple DL query, but how do we put them in some order?
    * If we want to get the events and subevents for a character, we either query this or we have
      a subchain (the subchain is great as it brings the named events in to the inferred props but what is the cost?)
      

    included o participant -> participant

* Pellet
    * Switch tests to Pellet
    * Review all performance decisions - now we've resolved the errors, we can get much faster classification/query times
        * allDifferent?
    
* Series
    * Rebels - series 4(16)
    * The Clone Wars series 7 series eps(22, 22, 22, 22, 20, 13, 12)
    * Bad Batch - 1 series eps(incomplete)

* Review properties
    * "visited" only used once - is it useful?
    * "healed" also only used once
    * "surrenderOf" - anywhere else to use this?
    