#!/bin/bash

echo "Enviando transações para http://localhost:8080/transaction..."

for i in {1..10}; do

  uuid=$(uuidgen)
  agencia=$((1000 + i))
  conta=$((200000 + i))
  hora=$((15 + (i / 2)))
  minuto=$(( (i % 2) * 30 ))
  data="2025-05-18T$(printf "%02d:%02d:00" $hora $minuto)"

  http POST http://localhost:8080/transaction \
    uuid="$uuid" \
    valor:=500.00 \
    data="$data" \
    conta:="{
      \"codigoAgencia\": $agencia,
      \"codigoConta\": $conta
    }" \
    beneficiario:="{
      \"CPF\": 98765432100,
      \"codigoBanco\": 341,
      \"agencia\": \"0001\",
      \"conta\": \"12345678-9\",
      \"nomeFavorecido\": \"João da Silva\"
    }" \
    tipoTransacao="TED" \
    situacao="ANALISADA"

done

echo "Envio finalizado."
