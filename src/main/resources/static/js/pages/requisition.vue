<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requisition</h1>

      <b-form-group label="Description:">
        <b-form-textarea
          id="description"
          disabled
          v-model="form.description"
          required
          placeholder="Description"
        ></b-form-textarea>
      </b-form-group>

      <b-form-group label="Creator:">
        <b-form-input id="creator" disabled v-model="form.creator" required placeholder="Creator"></b-form-input>
      </b-form-group>

      <b-form-group label="Assigned To:">
        <b-form-input
          id="assigned"
          disabled
          v-model="form.assigned"
          required
          placeholder="Assigned"
        ></b-form-input>
      </b-form-group>

      <b-form-group label="Status:">
        <b-form-input id="status" disabled v-model="form.status" required placeholder="Status"></b-form-input>
      </b-form-group>

      <b-form-group label="Due Date:">
        <b-form-input id="dueDate" disabled v-model="form.dueDate" type="date" required></b-form-input>
      </b-form-group>

      <b-form-group label="Creation Date:">
        <b-form-input id="creationDate" disabled v-model="form.creationDate" type="date" required></b-form-input>
      </b-form-group>
    </b-container>
  </c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {
      form: {
        description: "",
        creator: "",
        status: "new",
        creationDate: "",
        dueDate: "",
        assigned: ""
      },
      requisition: {}
    };
  },
  methods: {
    loadPage: function() {
      if (this.$route.params.id == "new") {
      } else {
        this.loadRequisition();
      }
    },
    loadRequisition: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .get("/requisitions/" + requisitonId)
        .then(function(response) {
          self.form = response.data;
        });
    }
  },
  mounted: function() {
    this.loadPage();
  },
  watch: {
    $route: function(to, from) {
      //this.loadPage();
    }
  }
};
</script>
