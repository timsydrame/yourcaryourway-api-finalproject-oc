--
-- PostgreSQL database dump
--

\restrict eJVkr7rYay5dGnI7RZZ4d0qwBQgjfdf5W5xUkNiXSjWolYkDvAju9r8cUtArFJh

-- Dumped from database version 15.18 (Debian 15.18-1.pgdg13+1)
-- Dumped by pg_dump version 15.18 (Debian 15.18-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: agencies; Type: TABLE; Schema: public; Owner: ycyw_user
--

CREATE TABLE public.agencies (
    id uuid NOT NULL,
    address character varying(255) NOT NULL,
    city character varying(100) NOT NULL,
    country character varying(2) NOT NULL,
    name character varying(100) NOT NULL,
    phone character varying(20)
);


ALTER TABLE public.agencies OWNER TO ycyw_user;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: ycyw_user
--

CREATE TABLE public.messages (
    id uuid NOT NULL,
    attachment_url character varying(500),
    content text NOT NULL,
    conversation_id character varying(100) NOT NULL,
    created_at timestamp with time zone,
    direction character varying(30) NOT NULL,
    is_read boolean NOT NULL,
    read_at timestamp(6) without time zone,
    subject character varying(255),
    user_id uuid,
    CONSTRAINT messages_direction_check CHECK (((direction)::text = ANY ((ARRAY['USER_TO_SUPPORT'::character varying, 'SUPPORT_TO_USER'::character varying])::text[])))
);


ALTER TABLE public.messages OWNER TO ycyw_user;

--
-- Name: offers; Type: TABLE; Schema: public; Owner: ycyw_user
--

CREATE TABLE public.offers (
    id uuid NOT NULL,
    is_available boolean NOT NULL,
    price_per_day numeric(10,2) NOT NULL,
    agency_departure_id uuid NOT NULL,
    agency_return_id uuid NOT NULL,
    vehicle_id uuid NOT NULL
);


ALTER TABLE public.offers OWNER TO ycyw_user;

--
-- Name: reservations; Type: TABLE; Schema: public; Owner: ycyw_user
--

CREATE TABLE public.reservations (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    departure_datetime timestamp(6) without time zone NOT NULL,
    return_datetime timestamp(6) without time zone NOT NULL,
    status character varying(20) NOT NULL,
    stripe_payment_id character varying(100),
    total_price numeric(10,2) NOT NULL,
    offer_id uuid NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT reservations_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'CONFIRMED'::character varying, 'MODIFIED'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.reservations OWNER TO ycyw_user;

--
-- Name: users; Type: TABLE; Schema: public; Owner: ycyw_user
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    is_active boolean,
    created_at timestamp(6) without time zone,
    email character varying(255) NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone
);


ALTER TABLE public.users OWNER TO ycyw_user;

--
-- Name: vehicles; Type: TABLE; Schema: public; Owner: ycyw_user
--

CREATE TABLE public.vehicles (
    id uuid NOT NULL,
    acriss_code character varying(4) NOT NULL,
    is_available boolean NOT NULL,
    brand character varying(50) NOT NULL,
    color character varying(30),
    created_at timestamp(6) without time zone,
    fuel_type character varying(20) NOT NULL,
    model character varying(50) NOT NULL,
    photo_url character varying(500),
    seats integer NOT NULL,
    transmission character varying(20) NOT NULL,
    updated_at timestamp(6) without time zone,
    year integer NOT NULL,
    agency_id uuid NOT NULL,
    CONSTRAINT vehicles_fuel_type_check CHECK (((fuel_type)::text = ANY ((ARRAY['ESSENCE'::character varying, 'DIESEL'::character varying, 'ELECTRIQUE'::character varying, 'HYBRIDE'::character varying])::text[]))),
    CONSTRAINT vehicles_seats_check CHECK (((seats >= 1) AND (seats <= 9))),
    CONSTRAINT vehicles_transmission_check CHECK (((transmission)::text = ANY ((ARRAY['MANUELLE'::character varying, 'AUTOMATIQUE'::character varying])::text[]))),
    CONSTRAINT vehicles_year_check CHECK (((year >= 1900) AND (year <= 2100)))
);


ALTER TABLE public.vehicles OWNER TO ycyw_user;

--
-- Name: agencies agencies_pkey; Type: CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.agencies
    ADD CONSTRAINT agencies_pkey PRIMARY KEY (id);


--
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: offers offers_pkey; Type: CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (id);


--
-- Name: reservations reservations_pkey; Type: CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: vehicles vehicles_pkey; Type: CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT vehicles_pkey PRIMARY KEY (id);


--
-- Name: idx_agency_city_country; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_agency_city_country ON public.agencies USING btree (city, country);


--
-- Name: idx_message_conversation; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_message_conversation ON public.messages USING btree (conversation_id);


--
-- Name: idx_message_created_at; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_message_created_at ON public.messages USING btree (created_at);


--
-- Name: idx_message_user; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_message_user ON public.messages USING btree (user_id);


--
-- Name: idx_offer_available; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_offer_available ON public.offers USING btree (is_available);


--
-- Name: idx_reservation_offer; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_reservation_offer ON public.reservations USING btree (offer_id);


--
-- Name: idx_reservation_status; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_reservation_status ON public.reservations USING btree (status);


--
-- Name: idx_reservation_user; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_reservation_user ON public.reservations USING btree (user_id);


--
-- Name: idx_vehicle_acriss; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_vehicle_acriss ON public.vehicles USING btree (acriss_code);


--
-- Name: idx_vehicle_available; Type: INDEX; Schema: public; Owner: ycyw_user
--

CREATE INDEX idx_vehicle_available ON public.vehicles USING btree (is_available);


--
-- Name: reservations fkb5g9io5h54iwl2inkno50ppln; Type: FK CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT fkb5g9io5h54iwl2inkno50ppln FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: offers fkd3tpimsjlxw84mrl13de834ed; Type: FK CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fkd3tpimsjlxw84mrl13de834ed FOREIGN KEY (agency_departure_id) REFERENCES public.agencies(id);


--
-- Name: reservations fkjk5eau9ty1r4m4ibk0tat5ulr; Type: FK CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT fkjk5eau9ty1r4m4ibk0tat5ulr FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- Name: offers fklx6v6eaab62aafhscs4gvjw6g; Type: FK CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fklx6v6eaab62aafhscs4gvjw6g FOREIGN KEY (vehicle_id) REFERENCES public.vehicles(id);


--
-- Name: vehicles fkmucussvr907ss1h4ko68h43b9; Type: FK CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT fkmucussvr907ss1h4ko68h43b9 FOREIGN KEY (agency_id) REFERENCES public.agencies(id);


--
-- Name: offers fkn977tn3o6gums95o89vsma3e9; Type: FK CONSTRAINT; Schema: public; Owner: ycyw_user
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT fkn977tn3o6gums95o89vsma3e9 FOREIGN KEY (agency_return_id) REFERENCES public.agencies(id);


--
-- PostgreSQL database dump complete
--

\unrestrict eJVkr7rYay5dGnI7RZZ4d0qwBQgjfdf5W5xUkNiXSjWolYkDvAju9r8cUtArFJh

