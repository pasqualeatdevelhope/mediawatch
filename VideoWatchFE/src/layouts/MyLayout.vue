<template>
  <q-layout view="lHh Lpr lFf">
    <q-layout-header>
      <q-toolbar color="primary" :glossy="$q.theme === 'mat'" :inverted="$q.theme === 'ios'">
        <q-btn flat dense round icon="arrow_back" v-show="showGoBack" @click="goBack"/>
        <q-toolbar-title>Videoteca
          <div slot="subtitle">Le smart TV sucano</div>
        </q-toolbar-title>
        <div style="margin-right:30px">
          <q-btn flat dense @click="$root.$emit('cerca')" label="Cerca"></q-btn>
        </div>
        <div>
          <q-btn flat dense @click="$root.$emit('aggiungi')" label="Aggiungi"></q-btn>
        </div>
      </q-toolbar>
    </q-layout-header>
    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script>
import { openURL } from "quasar";

export default {
  name: "MyLayout",
  data() {
    return {
      showGoBack: false
    };
  },
  methods: {
    goBack() {
      this.$root.$emit("goBack");
    }
  },
  mounted() {
    this.$root.$on("showGoBack", tick => {
      this.showGoBack = true;
    });
    this.$root.$on("hideGoBack", tick => {
      this.showGoBack = false;
    });
  },
  destroyed() {
    this.$root.$off("showGoBack");
    this.$root.$off("hideGoBack");
  }
};
</script>

<style>
</style>
