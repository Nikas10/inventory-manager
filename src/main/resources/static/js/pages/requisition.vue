<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requisition</h1>

      <b-form-group label="Description:">
        <b-form-textarea
          id="description"
          :disabled="!changesAllowed"
          v-model="form.description"
          required
          placeholder="Description"
        ></b-form-textarea>
      </b-form-group>

      <b-form-group label="Creator:">
        <b-form-input id="creator" disabled v-model="form.login" required placeholder="Creator"></b-form-input>
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
        <b-form-input
          id="dueDate"
          :disabled="!changesAllowed"
          v-model="form.dueDate"
          type="date"
          required
        ></b-form-input>
      </b-form-group>

      <b-form-group label="Creation Date:">
        <b-form-input
          id="creationDate"
          :disabled="!changesAllowed"
          v-model="form.creationDate"
          type="date"
          required
        ></b-form-input>
      </b-form-group>

      <b-button v-if="approveAllowed" @click="setStatusApproved">Approve</b-button>

      <b-button
        v-if="clarificationAllowed"
        @click="setStatusRequiredClarification"
      >Require Clarification</b-button>

      <b-button v-if="rejectAllowed" @click="setStatusRejected">Reject</b-button>

      <b-button v-if="createAllowed" @click="setStatusCompleted">Complete</b-button>

      <b-button v-if="completeAllowed" @click="setStatusCompletedChanges">Complete Changes</b-button>

      <b-button v-if="createAllowed" @click="createRequisition">Create</b-button>

      <b-button v-if="updateAllowed" @click="updateRequisition">Update</b-button>

      <h2>Requested Positions</h2>

      <b-table small :items="positions" :fields="positionsFields"></b-table>
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
        login: "",
        status: "NEW",
        creationDate: this.formatDate(Date.now()),
        dueDate: this.formatDate(new Date().setDate(new Date().getDate() + 14)),
        assigned: ""
      },
      positionsFields: {
        name: {
          label: "Name",
          sortable: true
        },
        amount: {
          label: "Amount",
          sortable: true
        }
      },
      requisition: {},
      positions: []
    };
  },
  computed: {
    isStaff: function() {
      return (isStaff = ["staff", "admin"].includes(this.storage.user.role));
    },
    changesAllowed: function() {
      if (!this.storage.user) {
        return false;
      }

      const isNew = this.form.status == "NEW";
      const needsClarification = this.form.status == "REQUIRED_CLARIFICATION";
      const isStaff = ["staff", "admin"].includes(this.storage.user.role);

      return isNew || needsClarification || isStaff;
    },
    updateAllowed: function() {
      const isNew = this.form.status == "NEW";
      const isCompleted = this.form.status == "COMPLETED";
      const isApproved = this.form.status == "APPROVED";

      return (!isNew || !isCompleted || !isApproved) && isStaff;
    },
    approveAllowed: function() {
      const reviewNeeded = this.form.status == "REVIEW_NEEDED";

      return reviewNeeded && this.isStaff;
    },
    rejectAllowed: function() {
      const reviewNeeded = this.form.status == "REVIEW_NEEDED";

      return reviewNeeded && this.isStaff;
    },
    clarificationAllowed: function() {
      const reviewNeeded = this.form.status == "REVIEW_NEEDED";

      return reviewNeeded && this.isStaff;
    },
    completeAllowed: function() {},
    createAllowed: function() {
      const isNew = this.form.status == "NEW";

      return isNew;
    }
  },
  methods: {
    appendZeroes: function(n) {
      if (n <= 9) {
        return "0" + n;
      }
      return n;
    },
    formatDate(string) {
      let date = new Date(string);

      let year = date.getFullYear();
      let month = this.appendZeroes(date.getMonth() + 1);
      let day = this.appendZeroes(date.getDate());

      return year + "-" + month + "-" + day;
    },
    loadPage: function() {
      if (this.$route.params.id != "new") {
        this.loadRequisition();
        this.loadPositions();
      }
    },
    loadRequisition: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .get("/requisitions/" + requisitonId)
        .then(function(response) {
          self.form = response.data;
          self.form.dueDate = self.formatDate(response.data.dueDate);
          self.form.creationDate = self.formatDate(response.data.creationDate);
        });
    },
    loadPositions: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .get("/requisitions/" + requisitonId + "/positions/")
        .then(function(response) {
          self.positions = response.data;
        });
    },
    setStatusApproved: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "APPROVED" })
        .then(function(response) {});
    },
    setStatusRequiredClarification: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, {
          status: "REQUIRED_CLARIFICATION"
        })
        .then(function(response) {});
    },
    setStatusRejected: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "REJECTED" })
        .then(function(response) {});
    },
    setStatusCompleted: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "COMPLETED" })
        .then(function(response) {});
    },
    setStatusCompletedChanges: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "REVIEW_NEEDED" })
        .then(function(response) {});
    },
    createRequisition: function() {
      const self = this;

      this.$server
        .post("/requisitions/", this.form)
        .then(function(response) {});
    }
  },
  mounted: function() {
    this.loadPage();
  },
  watch: {
    $route: function(to, from) {
      this.loadPage();
    }
  }
};
</script>
