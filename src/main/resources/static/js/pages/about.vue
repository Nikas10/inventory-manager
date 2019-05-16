<template>
  <c-default-page :storage="storage"></c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {};
  },
  methods: {
    loadReport: function() {
      this.$server.get("report").then(function(response) {    
      });
    }
  },
  mounted: function() {
    this.loadReport();
  }
}
</script>
