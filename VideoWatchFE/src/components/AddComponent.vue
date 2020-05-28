<template>
  <div class="absolute-top" style="margin: 20px">
    <q-tabs>
      <q-tab name="xtab-1" icon="add" slot="title" label="Film" default/>
      <q-tab name="xtab-2" icon="add" slot="title" label="Serie"/>
      <q-tab name="xtab-3" icon="add" slot="title" label="Stagione"/>
      <q-tab name="xtab-4" icon="add" slot="title" label="Episodio"/>

      <q-tab-pane name="xtab-1">
        <AddFilmComponent/>
      </q-tab-pane>
      <q-tab-pane name="xtab-2">
        <AddSerieComponent/>
      </q-tab-pane>
      <q-tab-pane name="xtab-3">
        <AddSeasonComponent/>
      </q-tab-pane>
      <q-tab-pane name="xtab-4">
        <AddEpisodeComponent/>
      </q-tab-pane>
    </q-tabs>
  </div>
</template>



<script>
import AddFilmComponent from "components/AddFilmComponent";
import AddSerieComponent from "components/AddSerieComponent";
import AddSeasonComponent from "components/AddSeasonComponent";
import AddEpisodeComponent from "components/AddEpisodeComponent";

export default {
  name: "AddComponent",
  components: {
    AddFilmComponent,
    AddSerieComponent,
    AddSeasonComponent,
    AddEpisodeComponent
  },
  data() {
    return {};
  }
};
</script>