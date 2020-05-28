<template>
  <div class="absolute-top" style="margin: 20px">
    <q-tabs>
      <q-tab name="xtab-1" icon="search" slot="title" label="Film" default/>
      <q-tab name="xtab-2" icon="search" slot="title" label="Serie"/>
      <q-tab-pane name="xtab-1">
        <SearchFilmComponent />
      </q-tab-pane>
      <q-tab-pane name="xtab-2">
        <SearchSerieComponent />
      </q-tab-pane>
    </q-tabs>
  </div>
</template>


<script>
import SearchFilmComponent from "components/SearchFilmComponent";
import SearchSerieComponent from "components/SearchSerieComponent";

export default {
  name: "SearchComponent",
  components: { SearchFilmComponent, SearchSerieComponent },
  data(){
    return { }
  }
};
</script>