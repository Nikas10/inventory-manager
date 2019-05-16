<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requisition</h1>

      <b-form>
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
      </b-form>

      <b-form-group>
        <b-button v-if="approveAllowed" @click="setStatusApproved">Approve</b-button>

        <b-button
          v-if="clarificationAllowed"
          @click="setStatusRequiredClarification"
        >Require Clarification</b-button>

        <b-button v-if="rejectAllowed" @click="setStatusRejected">Reject</b-button>

        <b-button v-if="completeAllowed" @click="setStatusCompleted">Complete</b-button>

        <b-button v-if="completeChangeAllowed" @click="setStatusCompletedChanges">Complete Changes</b-button>

        <b-button v-if="createAllowed" @click="createRequisition">Create</b-button>

        <b-button v-if="updateAllowed" @click="updateRequisition">Update</b-button>
      </b-form-group>
      <h2>Requested Positions</h2>

      <b-form inline>
        <b-form-input v-model="newPosition.amount" type="number" min="1"></b-form-input>
        <b-col>
          <b-form-select v-model="newPosition.id" :options="positionOptions"></b-form-select>
        </b-col>
        <b-button variant="primary" @click="addNewPosition">Add position</b-button>
      </b-form>

      <br>

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
        amount: {
          label: "Amount",
          sortable: true
        },
        name: {
          label: "Name",
          sortable: true
        },
        actions: {
          label: "Actions",
          sortable: true
        }
      },
      requisition: {},
      positions: [],
      newPosition: {
        amount: 1,
        id: "0000-0001"
      },
      availablePositions: [
        {
          id: "0000-0001",
          name: "Pos 1"
        },
        {
          id: "0000-0002",
          name: "Pos 2"
        },
        {
          id: "0000-0003",
          name: "Pos 3"
        }
      ]
    };
  },
  computed: {
    isStaff: function() {
      if (!this.storage.user) {
        return false;
      }

      return ["staff", "admin"].includes(this.storage.user.role);
    },
    changesAllowed: function() {
      if (!this.storage.user) {
        return false;
      }

      const isNew = this.form.status == "NEW";
      const needsClarification = this.form.status == "REQUIRED_CLARIFICATION";

      return isNew || needsClarification || this.isStaff;
    },
    updateAllowed: function() {
      const isNew = this.form.status == "NEW";
      const isCompleted = this.form.status == "COMPLETED";
      const isApproved = this.form.status == "APPROVED";

      return (!isNew || !isCompleted || !isApproved) && this.isStaff;
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
    completeChangeAllowed: function() {},
    createAllowed: function() {
      const isNew = this.form.status == "NEW";

      return isNew;
    },
    positionOptions: function() {
      var a = [];
      return this.availablePositions.map(function(pos) {
        return { value: pos.id, text: pos.name };
      });
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
        this.loadAvailablePositions();
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
    loadAvailablePositions: function() {
      // TODO GET /api/accounts/{id}/availablePositions/
      // TODO присвоить amount.id первый id из запроса
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
    },
    addNewPosition: function() {
      // TODO сделать копию positions, чтобы потом по разнице определять какие запросы нужно отправить
      // TODO добавлять новые записи в копию
    },
    updateRequisition: function() {
      // TODO
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
