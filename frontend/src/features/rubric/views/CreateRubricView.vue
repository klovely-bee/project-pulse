<script setup>
import { reactive, ref } from 'vue'
import { createRubric } from '../services/rubricApi'

const createEmptyCriterion = () => ({
  criterionName: '',
  description: '',
  maxScore: '',
})

const form = reactive({
  name: '',
  description: '',
  criteria: [createEmptyCriterion()],
})

const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

function addCriterion() {
  form.criteria.push(createEmptyCriterion())
}

function removeCriterion(index) {
  if (form.criteria.length === 1) {
    return
  }

  form.criteria.splice(index, 1)
}

function resetForm() {
  form.name = ''
  form.description = ''
  form.criteria = [createEmptyCriterion()]
}

async function handleSubmit() {
  isSubmitting.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    const payload = {
      name: form.name,
      description: form.description,
      criteria: form.criteria.map((criterion) => ({
        criterionName: criterion.criterionName,
        description: criterion.description,
        maxScore: Number(criterion.maxScore),
      })),
    }

    await createRubric(payload)
    successMessage.value = 'Rubric created successfully.'
    resetForm()
  } catch (error) {
    errorMessage.value = error.response?.data?.message ?? 'Failed to create rubric.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="page-container">
    <div class="card">
      <h1 class="page-title">Create Rubric</h1>
      <p class="page-subtitle">Add a rubric and its grading criteria.</p>

      <form class="form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="rubric-name">Name</label>
          <input id="rubric-name" v-model="form.name" class="input" required />
        </div>

        <div class="form-group">
          <label for="rubric-description">Description</label>
          <textarea
            id="rubric-description"
            v-model="form.description"
            class="input textarea"
            rows="3"
          />
        </div>

        <div class="form-section">
          <div class="section-header">
            <h2 class="section-title">Criteria</h2>
            <button class="button button-secondary" type="button" @click="addCriterion">
              Add Criterion
            </button>
          </div>

          <div
            v-for="(criterion, index) in form.criteria"
            :key="index"
            class="subcard"
          >
            <div class="form-group">
              <label :for="`criterion-name-${index}`">Criterion Name</label>
              <input
                :id="`criterion-name-${index}`"
                v-model="criterion.criterionName"
                class="input"
                required
              />
            </div>

            <div class="form-group">
              <label :for="`criterion-description-${index}`">Description</label>
              <textarea
                :id="`criterion-description-${index}`"
                v-model="criterion.description"
                class="input textarea"
                rows="3"
              />
            </div>

            <div class="form-group">
              <label :for="`criterion-score-${index}`">Max Score</label>
              <input
                :id="`criterion-score-${index}`"
                v-model="criterion.maxScore"
                class="input"
                min="0"
                required
                type="number"
              />
            </div>

            <button
              class="button button-secondary"
              :disabled="form.criteria.length === 1"
              type="button"
              @click="removeCriterion(index)"
            >
              Remove Criterion
            </button>
          </div>
        </div>

        <p v-if="successMessage" class="message success">{{ successMessage }}</p>
        <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>

        <div class="form-actions">
          <button class="button" :disabled="isSubmitting" type="submit">
            {{ isSubmitting ? 'Creating...' : 'Create Rubric' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
