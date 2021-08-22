package br.com.orange.carteira.cartoes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NovoCartaoRequest {

    @NotBlank
    @NotNull
    private String numeroCartao;

    @JsonProperty
    @Enumerated(EnumType.STRING)
    private TipoCartaoEnum tipoCartaoEnum;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)// Gera Json da propriedade
    public NovoCartaoRequest(@NotBlank @NotNull String numeroCartao, TipoCartaoEnum tipoCartaoEnum) {
        this.numeroCartao = numeroCartao;
        this.tipoCartaoEnum = tipoCartaoEnum;
    }

    public Cartao paraCartao() {
        return new Cartao(this.numeroCartao, this.tipoCartaoEnum);
    }


    public String getNumeroCartao() {
        return numeroCartao;
    }

    public TipoCartaoEnum getTipoCartaoEnum() {
        return tipoCartaoEnum;
    }

    @Override
    public String toString() {
        return "NovoCartaoRequest{" +
                "numeroCartao='" + numeroCartao + '\'' +
                ", tipoCartaoEnum=" + tipoCartaoEnum +
                '}';
    }
}
