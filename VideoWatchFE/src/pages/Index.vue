<template>
  <q-page class="flex flex-center">
    <div v-show="showAdd">
      <AddComponent />
    </div>
    <div v-show="showSearch">
      <SearchComponent />
    </div>
  </q-page>
</template>

<style>
</style>

<script>
import AddComponent from 'components/AddComponent';
import SearchComponent from 'components/SearchComponent';

export default {
  name: "PageIndex",
  components: { AddComponent, SearchComponent },
  data() {
    return {
      showAdd: false,
      showSearch: true
    };
  },
  mounted() {
    this.$root.$on("cerca", async tick => {
      this.showAdd = false;
      this.showSearch = true;
    });
    this.$root.$on("aggiungi", async tick => {
      this.showAdd = true;
      this.showSearch = false;
      this.$root.$emit('hideGoBack');
    });
  }
};
</script>
