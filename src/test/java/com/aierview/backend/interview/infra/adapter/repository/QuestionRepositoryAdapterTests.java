package com.aierview.backend.interview.infra.adapter.repository;

import com.aierview.backend.interview.domain.contract.repository.IQuestionRepository;
import com.aierview.backend.interview.domain.entity.Question;
import com.aierview.backend.interview.infra.mapper.QuestionMapper;
import com.aierview.backend.interview.infra.persistence.entity.QuestionJpaEntity;
import com.aierview.backend.interview.infra.persistence.repository.QuestionJpaRepository;
import com.aierview.backend.shared.testdata.InterviewTestFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class QuestionRepositoryAdapterTests {
    private IQuestionRepository questionRepository;
    private QuestionMapper questionMapper;
    private QuestionJpaRepository questionJpaRepository;

    @BeforeEach
    void setUp() {
        this.questionMapper = Mockito.mock(QuestionMapper.class);
        this.questionJpaRepository = Mockito.mock(QuestionJpaRepository.class);
        this.questionRepository = new QuestionRepositoryAdapter(questionMapper, questionJpaRepository);
    }

    @Test
    @DisplayName("Should save question")
    void shouldSaveQuestion() {
        Question toSaveQuestion = InterviewTestFixture.anyQuestion();
        QuestionJpaEntity toSaveEntity = InterviewTestFixture.anyQuestionJpaList(List.of(toSaveQuestion)).get(0);

        Question savedQuestion = InterviewTestFixture.anySavedQuestion(toSaveQuestion);
        QuestionJpaEntity savedEntity = InterviewTestFixture.anySavedQuestionJpaList(List.of(savedQuestion)).get(0);

        Mockito.when(this.questionMapper.mapToJpa(toSaveQuestion)).thenReturn(toSaveEntity);
        Mockito.when(this.questionJpaRepository.save(toSaveEntity)).thenReturn(toSaveEntity);

        this.questionRepository.save(toSaveQuestion);

        Mockito.verify(this.questionMapper, Mockito.times(1)).mapToJpa(toSaveQuestion);
        Mockito.verify(this.questionJpaRepository, Mockito.times(1)).save(toSaveEntity);
    }

    @Test
    @DisplayName("Should save question list an return")
    void shouldSaveQuestionListAnReturn() {
        List<Question> toSaveQuestionList = InterviewTestFixture.anyQuestionList();
        List<QuestionJpaEntity> toSaveQuestionJpaList = InterviewTestFixture.anyQuestionJpaList(toSaveQuestionList);

        List<QuestionJpaEntity> savedQuestionJpaList = InterviewTestFixture.anySavedQuestionJpaList(toSaveQuestionList);
        List<Question> savedQuestionList = InterviewTestFixture.anySavedQuestionList(savedQuestionJpaList);

        Mockito.when(this.questionMapper.mapToListJpa(toSaveQuestionList)).thenReturn(toSaveQuestionJpaList);
        Mockito.when(this.questionJpaRepository.saveAll(toSaveQuestionJpaList)).thenReturn(savedQuestionJpaList);
        Mockito.when(this.questionMapper.mapToListEntity(savedQuestionJpaList)).thenReturn(savedQuestionList);

        List<Question> savedQuestions = questionRepository.saveAll(toSaveQuestionList);

        Assertions.assertThat(savedQuestions).hasSize(toSaveQuestionJpaList.size());

        Mockito.verify(this.questionMapper, Mockito.times(1)).mapToListJpa(toSaveQuestionList);
        Mockito.verify(this.questionJpaRepository, Mockito.times(1)).saveAll(toSaveQuestionJpaList);
        Mockito.verify(this.questionMapper, Mockito.times(1)).mapToListEntity(savedQuestionJpaList);
    }

    @Test
    @DisplayName("Should return optional of empty when question does not exist")
    void shouldReturnOptionalOfEmptyWhenQuestionDoesNotExist() {
        Long questionId = 1L;
        Mockito.when(this.questionJpaRepository.findById(questionId)).thenReturn(Optional.empty());
        Optional<Question> question = questionRepository.findById(questionId);
        Assertions.assertThat(question).isEmpty();
        Mockito.verify(this.questionJpaRepository, Mockito.times(1)).findById(questionId);
    }

    @Test
    @DisplayName("Should return optional of Question if exists on findbyid")
    void shouldReturnOptionalOfQuestionIfQuestionExistsOnFindbyid() {
        Question question = InterviewTestFixture.anyQuestion();
        QuestionJpaEntity questionJpaEntity = InterviewTestFixture.anyQuestionJpaList(List.of(question)).get(0);
        Question savedQuestion = InterviewTestFixture.anySavedQuestion(question);
        QuestionJpaEntity savedJpaQuestion = InterviewTestFixture.anySavedQuestionJpaList(List.of(savedQuestion)).get(0);

        Mockito.when(this.questionJpaRepository.findById(question.getId())).thenReturn(Optional.of(questionJpaEntity));
        Mockito.when(this.questionMapper.mapToEntity(Mockito.any(QuestionJpaEntity.class))).thenReturn(savedQuestion);
        Optional<Question> result = questionRepository.findById(question.getId());
        Assertions.assertThat(result).isNotEmpty();
        Mockito.verify(this.questionJpaRepository, Mockito.times(1)).findById(question.getId());
    }

}
